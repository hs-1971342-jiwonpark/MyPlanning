package com.example.designsystem.component.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.designsystem.theme.SubMain
import com.example.designsystem.theme.WhiteAlpha65

@Composable
fun CompactSearchTextField() {
    var text by remember { mutableStateOf("") }

    BasicTextField(
        maxLines = 1,
        singleLine = true,
        value = text,
        onValueChange = { text = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(SubMain)
            .padding(horizontal = 16.dp, vertical = 12.dp)
        ,
        textStyle = MaterialTheme.typography.labelMedium.copy(
            color = WhiteAlpha65
        ),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                // 플레이스홀더와 실제 텍스트 필드가 동일한 위치에 렌더링되도록 설정
                if (text.isEmpty()) {
                    Text(
                        text = "키워드 또는 소유자 이름 입력",
                        style = MaterialTheme.typography.labelMedium,
                        color = WhiteAlpha65,
                        modifier = Modifier.padding(start = 0.dp) // 패딩 위치 통일
                    )
                }
                innerTextField() // 실제 텍스트 필드 렌더링
            }
        }
    )
}

@Composable
fun PlanetTitleTextField(text: String, onValueChange: (String) -> Unit) {

    BasicTextField(
        maxLines = 1,
        singleLine = true,
        value = text,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50.dp))
            .background(SubMain)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        textStyle = MaterialTheme.typography.labelMedium.copy(
            color = WhiteAlpha65
        ),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                // 플레이스홀더와 실제 텍스트 필드가 동일한 위치에 렌더링되도록 설정
                if (text.isEmpty()) {
                    Text(
                        text = "행성 이름 입력",
                        style = MaterialTheme.typography.labelMedium,
                        color = WhiteAlpha65,
                        modifier = Modifier.padding(start = 0.dp) // 패딩 위치 통일
                    )
                }
                innerTextField() // 실제 텍스트 필드 렌더링
            }
        }
    )
}

@Composable
fun PlanetDescriptionTextField(text: String, onValueChange: (String) -> Unit, modifier: Modifier) {

    BasicTextField(
        value = text,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(SubMain)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        textStyle = MaterialTheme.typography.labelMedium.copy(
            color = WhiteAlpha65
        ),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // 플레이스홀더와 실제 텍스트 필드가 동일한 위치에 렌더링되도록 설정
                if (text.isEmpty()) {
                    Text(
                        text = "행성 설명 입력",
                        style = MaterialTheme.typography.labelMedium,
                        color = WhiteAlpha65,
                        modifier = Modifier
                            .padding(start = 0.dp) // 패딩 위치 통일
                    )
                }
                innerTextField() // 실제 텍스트 필드 렌더링
            }
        }
    )
}

@Preview
@Composable
fun PrevSearchTextView() {
    PlanetDescriptionTextField(
        text = "",
        onValueChange = {},
        modifier = Modifier
    )
}