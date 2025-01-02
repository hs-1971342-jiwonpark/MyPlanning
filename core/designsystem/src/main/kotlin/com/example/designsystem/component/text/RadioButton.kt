package com.example.designsystem.component.text

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.data.model.ToggleComponentInfo

@Composable
fun Toggles(modifier: Modifier = Modifier) {
    val radioButtons = remember {
        mutableStateListOf(
            ToggleComponentInfo(
                isChecked = true,
                text = "최신순"
            ),
            ToggleComponentInfo(
                isChecked = false,
                text = "참여자 순"
            )
        )
    }
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
    ) {
        radioButtons.forEachIndexed { _, info ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable {
                        radioButtons.replaceAll {
                            it.copy(
                                isChecked = (it.text == info.text)
                            )
                        }
                    }
            ) {
                RadioButton(
                    selected = info.isChecked,
                    onClick = {
                        radioButtons.replaceAll {
                            it.copy(
                                isChecked = (it.text == info.text)
                            )
                        }
                    }
                )
                Text(
                    modifier = Modifier
                        .offset(x = (-6).dp),
                    style = MaterialTheme.typography.labelMedium,
                    text = info.text,
                    color = Color.White
                )
            }
        }
    }
}