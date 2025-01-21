package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.data.model.User
import com.example.data.model.UserCard
import com.example.data.model.UserWithCards

@Dao
interface UserWithCardsDao {

    @Transaction
    @Query("SELECT * FROM USER WHERE id = :userId")
    suspend fun getUserWithCards(userId: Int): UserWithCards

}