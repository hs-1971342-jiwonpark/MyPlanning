package com.example.designsystem.component.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.designsystem.theme.main
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen(modifier: Modifier = Modifier, text : String) {
    var dotCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            dotCount = (dotCount + 1) % 4
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(main),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "$text 중 입니다${".".repeat(dotCount)}",
            style = MaterialTheme.typography.labelMedium,
            color = Color.White
        )
    }
}