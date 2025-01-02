package com.example.data.di

import android.util.Log
import com.example.data.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        Log.d("FirebaseModule", "Initializing FirebaseFirestore instance")
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideUserRepository(firestore: FirebaseFirestore): UserRepository {
        Log.d("FirebaseModule", "Initializing FirebaseFirestore instance")
        return UserRepository(firestore)
    }
}
