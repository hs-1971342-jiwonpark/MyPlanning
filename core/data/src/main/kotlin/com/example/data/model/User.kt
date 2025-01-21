package com.example.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
@Entity(
    tableName = "USER",
    indices = [
        Index(value = ["email"], unique = true),
        Index(value = ["uid"], unique = true)
    ]
)
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val exp: Int = 0,
    val accessToken : String? = ""
) : Parcelable {
    constructor() : this(0, "", "", "", "", 0,"")
}