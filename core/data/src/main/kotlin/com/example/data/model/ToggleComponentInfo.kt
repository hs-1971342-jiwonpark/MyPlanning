package com.example.data.model

data class ToggleComponentInfo(
    val isChecked: Boolean,
    val text: String,
    val clickable : () -> Unit
)