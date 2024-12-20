package com.example.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.designsystem.theme.WhiteAlpha65
import com.example.designsystem.theme.main

@Composable
fun MyPageScreen() {
    val menuList = listOf(
        "내 정보",
        "이용 약관",
        "보유 행성"
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(main)
    ) {
        menuList.forEach {
            MenuCard(it)
        }
    }
}

@Composable
fun MenuCard(menuName: String) {
    Column(
        modifier = Modifier.background(main)
    ) {
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RectangleShape,
            colors = ButtonColors(
                containerColor = main,
                disabledContentColor = WhiteAlpha65,
                disabledContainerColor = main,
                contentColor = WhiteAlpha65
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    style = MaterialTheme.typography.bodyLarge,
                    text = menuName,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd),
                    tint = WhiteAlpha65
                )
            }
        }
    }
    HorizontalDivider(
        thickness = 2.dp,
        color = Color.Black
    )
}

@Preview
@Composable
fun ViewByMyPage() {
    MyPageScreen()
}