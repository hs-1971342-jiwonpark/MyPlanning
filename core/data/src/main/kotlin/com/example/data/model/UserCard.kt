package com.example.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Serializable
@Parcelize
@Entity(
    tableName = "USER_CARD",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["userId"])]
)
data class UserCard(
    @PrimaryKey(autoGenerate = true)
    var cid : Int = 0,
    var userId: String? = "",
    var image : String,
    var participatePeople: String = "",
    var keyWord: String = "",
    var description : String = "",
    var ownerName : String = "",
    var ownerProfile: String = ""
) : Parcelable {
    constructor() : this(0,"","", "", "","","")
}