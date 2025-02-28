package com.example.data.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

data class CommentUser(
    var coId: Int = 0,
    var cid: String = "",
    var name: String = "",
    var profile: String = "",
    var body: CommentBody = CommentBody()
)

data class CommentBody(
    val cbId: Int = 0,
    var image : String? = null,
    var comment : String? = ""
)