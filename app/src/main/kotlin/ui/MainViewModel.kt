package ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel  @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn



    private fun checkLoginStatus() {
        viewModelScope.launch {
            val loggedIn = true
            _isLoggedIn.value = loggedIn
        }
    }


    init {
        Log.d("LoginViewModel", "Firestore instance: $firestore")
        Log.d("LoginViewModel", "UserRepository instance: $userRepository")
    }

}

