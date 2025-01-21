package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.dao.UserCardDao
import com.example.data.dao.UserWithCardsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRoomDataBase(@ApplicationContext context : Context) =
        Room.databaseBuilder(
            context,
            UserDatabase::class.java,
            "user_db"
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideUserDao(database : UserDatabase) = database.UserDao()

    @Provides
    @Singleton
    fun provideUserCardDao(database: UserDatabase): UserCardDao = database.userCardDao()

    @Provides
    @Singleton
    fun provideUserWithCardsDao(database: UserDatabase): UserWithCardsDao = database.userWithCardsDao()
}