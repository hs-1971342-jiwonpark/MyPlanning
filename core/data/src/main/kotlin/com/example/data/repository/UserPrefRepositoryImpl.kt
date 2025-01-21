package com.example.data.repository

import androidx.datastore.core.DataStore
import com.example.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPrefRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<User>
) : UserPrefRepository {

    override suspend fun getUserPrefs(): Flow<User> {
        return dataStore.data.map { it }
    }

    override suspend fun setUserPrefs(user: User) {
        dataStore.updateData { user }
    }
}