package com.example.data.di

import android.util.Log
import com.example.data.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
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
    fun provideFireStore(): FirebaseFirestore {
        Log.d("FirebaseModule", "Initializing FirebaseFireStore instance")
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFireStorage(): FirebaseStorage {
        Log.d("FirebaseModule", "Initializing FirebaseFireStorage instance")
        return FirebaseStorage.getInstance()
    }

    @Provides
    @Singleton
    fun provideUserRepository(firestore: FirebaseFirestore, fireStorage : FirebaseStorage): UserRepository {
        Log.d("FirebaseModule", "Initializing UserRepository instance")
        return UserRepository(firestore,fireStorage)
    }
}
