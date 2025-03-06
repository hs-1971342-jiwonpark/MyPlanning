package com.example.data.repository

import com.example.data.model.User
import kotlinx.coroutines.flow.Flow

interface UserPrefRepository {

    suspend fun getUserPrefs(): Flow<User>

    suspend fun setUserPrefs(user: User)

    suspend fun deleteUser(user: User)
}