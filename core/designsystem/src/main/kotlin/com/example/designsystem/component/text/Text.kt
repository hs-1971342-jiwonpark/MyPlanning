package com.example.designsystem.component.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun LoginText(text: String) {
    Text(
        style = MaterialTheme.typography.bodyMedium,
        text = text,
        color = Color.White
    )
}