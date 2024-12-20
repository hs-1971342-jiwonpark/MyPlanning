package com.example.designsystem.component.text

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.example.designsystem.R
import com.example.designsystem.theme.customPlanetItemText

@Composable
fun PlanetCard(itemSize: Dp, index: Int, name: String, keyWord: String, peoples: Int) {

    val imgList = listOf(
        R.drawable.img1,
        R.drawable.img2
    )

    Card(
        modifier = Modifier
            .size(itemSize)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = imgList[index % imgList.size]),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            NameSheet(
                Modifier
                    .background(Color.Black.copy(alpha = 0.5f)),
                text = "${name}님의 키워드 : $keyWord",
                alignment = Alignment.TopCenter
            )

            NameSheet(
                Modifier
                    .background(Color.Black.copy(alpha = 0.5f)),
                text = "${peoples}명 참여중",
                alignment = Alignment.BottomCenter
            )
        }
    }
}

@Composable
fun NameSheet(modifier: Modifier, text: String, alignment: Alignment) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = alignment
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (alignment == Alignment.BottomCenter) {
                Image(
                    painter = painterResource(id = R.drawable.ic_peoples),
                    contentDescription = null,
                    modifier = modifier
                        .size(16.dp)
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(end = 4.dp)
                )
            }

            // 텍스트 표시
            Text(
                text = text,
                color = Color.White,
                style = customPlanetItemText
            )
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun SurfacePlanetBox() {
    val columns = 2
    val horizontalSpacing = 24.dp
    val verticalSpacing = 24.dp

    val itemSize =
        (LocalConfiguration.current.screenWidthDp.dp - ((columns + 1) * horizontalSpacing)) / columns
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalSpacing),
        horizontalArrangement = Arrangement.spacedBy(horizontalSpacing), // 가로 간격
        verticalArrangement = Arrangement.spacedBy(verticalSpacing) // 세로 간격
    ) {
        // 아이템 반복 생성
        for (i in 1..10) {
            PlanetCard(itemSize, i, "박지원", "공부", 20)
        }
    }
}
