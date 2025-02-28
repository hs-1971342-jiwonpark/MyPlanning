package com.example.login

import android.content.ContentValues.TAG
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
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
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

    init {
        checkLoginStatus()
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


    fun performKakaoLogin(context: Context) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                // 카카오 로그인 시도
                UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                    if (error != null) {
                        Log.e(TAG, "로그인 실패", error)
                        _loginState.value = LoginState.Error("로그인 실패: ${error.message}")
                        return@loginWithKakaoTalk
                    }

                    if (token != null) {
                        Log.i(TAG, "로그인 성공 ${token.accessToken}")
                        fetchKakaoUserInfo()
                    } else {
                        _loginState.value = LoginState.Error("토큰을 받지 못했습니다.")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "예외 발생: ${e.message}")
                _loginState.value = LoginState.Error("에러 발생: ${e.message}")
            }
        }
    }

    private fun fetchKakaoUserInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
                _loginState.value = LoginState.Error("사용자 정보 요청 실패: ${error.message}")
                return@me
            }

            if (user != null) {
                viewModelScope.launch {
                    try {
                        val userInfo = User(
                            name = user.kakaoAccount?.profile?.nickname ?: "Unknown",
                            email = user.kakaoAccount?.email ?: "No Email",
                            photoUrl = user.kakaoAccount?.profile?.thumbnailImageUrl ?: "",
                            uid = user.id.toString(),
                            exp = 0
                        )

                        // Firestore에서 기존 사용자 확인
                        val existingUser = userRepository.getUser(userInfo.uid).firstOrNull()

                        if (existingUser != null) {
                            Log.i(TAG, "기존 사용자 로그인 성공: ${existingUser.name}")
                            _userData.value = existingUser
                            _loginState.value = LoginState.Success(existingUser)
                        } else {
                            Log.i(TAG, "새로운 사용자, Firestore에 저장 중")
                            userRepository.saveUser(userInfo)
                            _loginState.value = LoginState.Success(userInfo)
                        }

                        userPrefRepository.setUserPrefs(userInfo)

                    } catch (e: Exception) {
                        Log.e(TAG, "Firestore 저장 오류: ${e.message}")
                        _loginState.value = LoginState.Error("사용자 정보 저장 실패: ${e.message}")
                    }
                }
            } else {
                _loginState.value = LoginState.Error("사용자 정보를 가져올 수 없습니다.")
            }
        }
    }

}

