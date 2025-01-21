package com.example.myaccount

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.User
import com.example.data.repository.UserPrefRepository
import com.example.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyAccountViewModel @Inject constructor(
    private val userPrefRepository: UserPrefRepository
) : ViewModel() {

    private val _userPrefData = MutableStateFlow(User())
    val userPrefData: StateFlow<User> = _userPrefData

    init {
        fetchUserDataByPref()
    }

    //유저를 DataStore에서 가저옴
    private fun fetchUserDataByPref() {
        viewModelScope.launch {
            userPrefRepository.getUserPrefs().collect { user ->
                _userPrefData.value = user
            }
        }
    }
}
