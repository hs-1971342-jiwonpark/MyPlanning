package com.example.data.di

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStore
import androidx.datastore.dataStoreFile
import com.example.data.model.User
import com.example.data.repository.UserPrefRepository
import com.example.data.repository.UserPrefRepositoryImpl
import com.example.data.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    internal fun providesUserPreferencesDataStore(
        @ApplicationContext context: Context,
        userPreferencesSerializer: UserPreferencesSerializer,
    ): DataStore<User> =
        DataStoreFactory.create(
            serializer = userPreferencesSerializer
        ) {
            context.dataStoreFile("user_preferences.pb")
        }

    @Provides
    @Singleton
    fun provideUserPrefRepository(
        dataStore: DataStore<User>
    ): UserPrefRepository {
        return UserPrefRepositoryImpl(dataStore)
    }
}