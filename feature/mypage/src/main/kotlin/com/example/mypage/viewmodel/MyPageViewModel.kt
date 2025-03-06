package com.example.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.User
import com.example.data.repository.UserPrefRepository
import com.example.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userPrefRepository: UserPrefRepository
) : ViewModel() {

    private val _userData = MutableStateFlow<User?>(null)
    val userData: StateFlow<User?> = _userData

    private val _userPrefData = MutableStateFlow<User?>(null)
    val userPrefData: StateFlow<User?> = _userPrefData

    init {
        fetchUserDataByPref()
        fetchUserData()
    }

    //유저를 FirebaseDb에서 가저옴
    private fun fetchUserData() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            viewModelScope.launch {
                userRepository.getUser(currentUser.uid).collect { user ->
                    _userData.value = user
                }
            }
        }
    }

    //유저를 DataStore에서 가저옴
    private fun fetchUserDataByPref() {
        viewModelScope.launch {
            userPrefRepository.getUserPrefs().collect { user ->
                _userPrefData.value = user
            }
        }
        
    }

    fun userExit(){
        viewModelScope.launch {
            val user = userPrefRepository.getUserPrefs().first()
            userPrefRepository.deleteUser(user)
            userRepository.userExit(user.uid)
        }
    }
}
