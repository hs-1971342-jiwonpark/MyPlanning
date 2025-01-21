package com.example.designsystem.component.text

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CustomButton(modifier: Modifier, text : String, onClick : () -> Unit){
    Button(
        modifier = modifier
            .fillMaxWidth(),
        onClick = onClick
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}