package com.example.designsystem.component.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.designsystem.theme.main

@Composable
fun ErrorPage(text: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(main)
            .fillMaxSize()
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

