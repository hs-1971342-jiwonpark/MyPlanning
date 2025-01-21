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
interface UserCardDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserCard(userCard: UserCard): Long

    @Transaction
    @Query("SELECT * FROM USER_CARD WHERE cid = :cardId")
    suspend fun getUserCardById(cardId: Int): UserCard?

    @Transaction
    @Query("SELECT * FROM USER_CARD WHERE userId = :userId")
    suspend fun getCardsByUserId(userId: Int): List<UserCard>

    @Transaction
    @Delete
    suspend fun deleteUserCard(userCard: UserCard)
}