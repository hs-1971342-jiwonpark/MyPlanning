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
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firestore: FirebaseFirestore,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState
    private val _userData = MutableStateFlow<User?>(null)
    val userData: StateFlow<User?> = _userData
    private val credentialManager = CredentialManager.create(context)

    init {
        Log.d("유저", "UserRepository is injected: $userRepository")
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

                    // Firebase 인증 처리
                    FirebaseAuth.getInstance().signInWithCredential(
                        GoogleAuthProvider.getCredential(idToken, null)
                    ).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            FirebaseAuth.getInstance().currentUser?.let { user ->
                                val userInfo = User(
                                    name = user.displayName.toString(),
                                    email = user.email.toString(),
                                    photoUrl = user.photoUrl.toString(),
                                    accessToken = idToken
                                )
                                firestore.collection("users").document(user.uid)
                                    .set(userInfo)
                                    .addOnSuccessListener { Log.d("Firestore", "Success") }
                                    .addOnFailureListener { e -> Log.e("Firestore", "Failed", e) }

                                _loginState.value = LoginState.Success(userInfo)

                                Log.d("로그인", "Calling saveUser with: $userInfo")
                                userRepository.saveUser(userInfo) // 호출 전후에 로그 추가
                                Log.d("로그인", "saveUser executed successfully")
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
