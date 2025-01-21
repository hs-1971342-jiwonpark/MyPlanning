package com.example.data.di

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.dao.UserCardDao
import com.example.data.dao.UserDao
import com.example.data.dao.UserWithCardsDao
import com.example.data.model.User
import com.example.data.model.UserCard

@Database(
    entities = [
        User::class,
        UserCard::class,
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class UserDatabase : RoomDatabase() {
    abstract fun UserDao(): UserDao
    abstract fun userCardDao(): UserCardDao
    abstract fun userWithCardsDao(): UserWithCardsDao
}