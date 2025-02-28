package com.example.designsystem.component.text

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.designsystem.theme.SubMain
import com.example.designsystem.theme.WhiteAlpha65

@Composable
fun CompactSearchTextField(searchText: String, onSearchTextChange: (String) -> Unit) {

    BasicTextField(
        maxLines = 1,
        singleLine = true,
        value = searchText,
        onValueChange = { onSearchTextChange(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
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
                if (searchText.isEmpty()) {
                    Text(
                        text = "키워드 또는 소유자 이름 입력",
                        style = MaterialTheme.typography.labelMedium,
                        color = WhiteAlpha65,
                        modifier = Modifier.padding(start = 0.dp)
                    )
                }
                innerTextField()
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
                if (text.isEmpty()) {
                    Text(
                        text = "행성 이름 입력",
                        style = MaterialTheme.typography.labelMedium,
                        color = WhiteAlpha65,
                        modifier = Modifier.padding(start = 0.dp)
                    )
                }
                innerTextField()
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


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageTextField(
    img: Uri?,
    text: String,
    onValueChange: (String) -> Unit,
    onImageClick: () -> Unit,
    onMessageSend: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(SubMain)
            .padding(start = 16.dp, end = 16.dp)
    ) {
        // 이미지가 있을 경우 표시
        if (img != Uri.EMPTY) {
            Spacer(modifier = Modifier.height(4.dp))
            GlideImage(
                model = img,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        // 입력 필드 + 카메라 버튼을 수평 정렬
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(50.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 텍스트 입력 필드
            Box(
                modifier = Modifier.weight(1f) // 남은 공간을 모두 차지하도록 설정
            ) {
                BasicTextField(
                    value = text,
                    onValueChange = onValueChange,
                    textStyle = MaterialTheme.typography.labelMedium.copy(
                        color = WhiteAlpha65
                    ),
                    decorationBox = { innerTextField ->
                        Box(modifier = Modifier.fillMaxWidth()) {
                            if (text.isEmpty()) {
                                Text(
                                    text = "행성 설명 입력",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = WhiteAlpha65,
                                    modifier = Modifier.align(Alignment.CenterStart) // 왼쪽 정렬
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
            // 카메라 버튼 (배경을 투명하게 설정)
            IconButton(
                onClick = onImageClick,
                modifier = Modifier
                    .clip(CircleShape) // 둥근 모양 유지
                    .background(Color.Transparent)
            ) {
                Text(
                    text = "📷", // 아이콘 대신 텍스트 사용 가능
                    style = MaterialTheme.typography.labelMedium
                )
            }
            IconButton(
                onClick = onMessageSend,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Transparent)
            ) {
                Text(
                    text = "⬆\uFE0F",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}



@Preview
@Composable
fun ImageTextField() {
    PlanetDescriptionTextField(
        text = "",
        onValueChange = {},
        modifier = Modifier
    )
}