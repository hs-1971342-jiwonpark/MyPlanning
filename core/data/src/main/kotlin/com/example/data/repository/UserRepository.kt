package com.example.data.repository

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    init {
        Log.d("유저", "UserRepository is injected: $firestore")
    }

    fun saveUser(user: User) {
        Log.d("유저", "UserRepository is injected: $firestore")

        val userId = user.email
        firestore.collection("users").document(userId).set(user)
    }

    fun getUser(userId: String): Flow<User?> = flow {
        try {
            val document = firestore.collection("users").document(userId).get().await()
            emit(document.toObject(User::class.java))
        } catch (e: Exception) {
            emit(null) // 에러 처리
        }
    }
}
