package com.example.login

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.LoginState
import com.example.data.model.User
import com.example.data.repository.UserPrefRepository
import com.example.data.repository.UserRepository
import com.example.designsystem.BuildConfig
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firestore: FirebaseFirestore,
    private val userRepository: UserRepository,
    private val userPrefRepository: UserPrefRepository
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    private val _userData = MutableStateFlow<User?>(null)
    val userData: StateFlow<User?> = _userData

    private val credentialManager = CredentialManager.create(context)

    private val _isLoggedIn = MutableStateFlow(false) // 초기값은 로그아웃 상태
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _isReady = MutableStateFlow(false) // 초기값은 로그아웃 상태
    val isReady: StateFlow<Boolean> = _isReady

    init {
        checkLoginStatus()
    }

    fun updateIsReady(isReady: Boolean) {
        viewModelScope.launch {
            _isReady.emit(isReady)
        }
    }

    fun updateLoginState(loggedIn: Boolean) {
        viewModelScope.launch {
            _isLoggedIn.emit(loggedIn)
        }
    }

    private fun checkLoginStatus(): Boolean {
        return _isLoggedIn.value
    }

    fun logOut() {
        _isLoggedIn.value = false
    }

    fun performGoogleLogin() {
        viewModelScope.launch {
            try {
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setServerClientId(BuildConfig.API_KEY) // 웹 클라이언트 ID
                    .setFilterByAuthorizedAccounts(false)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                _loginState.value = LoginState.Loading
                val credentialResponse = credentialManager.getCredential(context, request)
                val credential = credentialResponse.credential

                if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)
                    val idToken = googleIdTokenCredential.idToken

                    val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

                    val exp = if (currentUserUid != null) {
                        try {
                            val userSnapshot = firestore.collection("users")
                                .document(currentUserUid)
                                .get()
                                .await()

                            userSnapshot.getLong("exp")?.toInt() ?: 0
                        } catch (e: Exception) {
                            Log.e("Firestore", "Error fetching exp value", e)
                            0
                        }
                    } else 0

                    FirebaseAuth.getInstance().signInWithCredential(
                        GoogleAuthProvider.getCredential(idToken, null)
                    ).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            FirebaseAuth.getInstance().currentUser?.let { user ->
                                val userInfo = User(
                                    name = user.displayName.toString(),
                                    email = user.email.toString(),
                                    photoUrl = user.photoUrl.toString(),
                                    uid = user.uid,
                                    exp = exp
                                )
                                userRepository.saveUser(userInfo) // 호출 전후에 로그 추가
                                _loginState.value = LoginState.Success(userInfo)
                                viewModelScope.launch {
                                    userPrefRepository.setUserPrefs(userInfo)
                                }
                                Log.d("LoginViewModel", "saveUser executed successfully")
                            } ?: run {
                                _loginState.value =
                                    LoginState.Error("Firebase authentication failed")
                            }
                        } else {
                            _loginState.value = LoginState.Error("Firebase authentication failed")
                        }
                    }
                } else {
                    _loginState.value = LoginState.Error("Unsupported credential type")
                }
            } catch (e: GetCredentialException) {
                _loginState.value = LoginState.Error(e.message ?: "Credential request failed")
            }
        }
    }
}

