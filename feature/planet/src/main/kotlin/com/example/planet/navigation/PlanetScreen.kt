package com.example.planet.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.util.lerp
import com.example.designsystem.component.text.CompactSearchTextField
import com.example.designsystem.component.text.PlanetCard
import com.example.designsystem.component.text.Toggles
import com.example.designsystem.theme.main
import com.example.planet.R
import kotlin.math.absoluteValue

@Composable
fun PlanetScreen() {
    val imgList = remember { listOf(R.drawable.img1, R.drawable.img2) }
    val pagerState = rememberPagerState(pageCount = { 10 })
    val configuration = LocalConfiguration.current
    val columns = 2
    val spacing = 24.dp
    val itemSize by remember(configuration.screenWidthDp) {
        derivedStateOf {
            (configuration.screenWidthDp.dp - ((columns + 1) * spacing)) / columns
        }
    }
    val lazyListState = rememberLazyListState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(main),
        state = lazyListState
    ) {
        item(key = "pager") {
            PagerSection(pagerState, imgList)
        }

        item(key = "search") {
            CompactSearchTextField()
        }

        item(key = "toggles") {
            Toggles(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }

        item(key = "grid") {
            FlowRowAsGridView(itemSize, spacing)
        }
    }
}

@Composable
private fun PagerSection(
    pagerState: PagerState,
    imgList: List<Int>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 48.dp)
        ) { page ->
            val pageOffset by remember(page, pagerState) {
                derivedStateOf {
                    (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                }
            }

            PagerCard(
                pageOffset = pageOffset,
                imageResId = imgList[page % imgList.size]
            )
        }

        PagerIndicator(pagerState)
    }
}

@Composable
private fun PagerCard(
    pageOffset: Float,
    imageResId: Int
) {
    val scale by remember(pageOffset) {
        derivedStateOf {
            lerp(
                start = 0.85f,
                stop = 1.15f,
                fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f)
            )
        }
    }

    val alpha by remember(pageOffset) {
        derivedStateOf {
            lerp(
                start = 0.5f,
                stop = 1f,
                fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f)
            )
        }
    }

    Card(
        Modifier
            .fillMaxWidth()
            .height(400.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
            }
            .padding(start = 8.dp, end = 8.dp, top = 36.dp, bottom = 32.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.BottomCenter)
                    .offset(y = (-16).dp)
                    .clip(CircleShape),
                tint = Color.White
            )
        }
    }
}

@Composable
private fun PagerIndicator(pagerState: PagerState) {
    Row(
        modifier = Modifier
            .wrapContentHeight()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val size = if (pagerState.currentPage == iteration) 12.dp else 8.dp
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .size(size)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRowAsGridView(
    itemSize: Dp,
    spacing: Dp
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spacing),
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        for (i in 1..10) {
            PlanetCard(itemSize, i, "박지원", "공부", 20)
        }
    }
}