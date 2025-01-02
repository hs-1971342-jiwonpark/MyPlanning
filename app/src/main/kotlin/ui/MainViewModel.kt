package ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.data.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel  @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val userRepository: UserRepository
) : ViewModel() {
    init {
        Log.d("LoginViewModel", "Firestore instance: $firestore")
        Log.d("LoginViewModel", "UserRepository instance: $userRepository")
    }
}