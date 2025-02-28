package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
enum class PostType {
    ME, OTHER, NOT
}