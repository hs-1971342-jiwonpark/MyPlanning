package com.example.planet.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.util.lerp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.navOptions
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.data.model.UserCard
import com.example.designsystem.R
import com.example.designsystem.component.text.CompactSearchTextField
import com.example.designsystem.component.text.ErrorPage
import com.example.designsystem.component.text.LoadingScreen
import com.example.designsystem.component.text.Toggles
import com.example.designsystem.theme.customPlanetItemText
import com.example.designsystem.theme.main
import com.example.planet.navigation.navigateToPreview
import com.example.planet.viewmodel.PlanetUiState
import com.example.planet.viewmodel.PlanetViewModel
import kotlin.math.absoluteValue

@Composable
internal fun PlanetScreen(
    navController: NavController,
    viewModel: PlanetViewModel
) {
    val stateCardList by viewModel.cardData.collectAsStateWithLifecycle()
    val postType by viewModel.postType.collectAsStateWithLifecycle()
    var cardId by rememberSaveable { mutableStateOf("") }
    val configuration = LocalConfiguration.current
    val columns = 2
    val spacing = 24.dp
    val pagerState = rememberPagerState(pageCount = { stateCardList.size })
    val itemSize by remember(configuration.screenWidthDp) {
        derivedStateOf {
            (configuration.screenWidthDp.dp - ((columns + 1) * spacing)) / columns
        }
    }
    val isReady by viewModel.isReady.collectAsState()
    val lazyListState = rememberLazyListState()
    val planetUiState by viewModel.planetUiState.collectAsState()

    val topLevelNavOptions = navOptions {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }

    LaunchedEffect(key1 = postType, key2 = isReady) {
        if (isReady && postType != PostType.NOT) {
            navController.navigateToPreview(postType, cardId, topLevelNavOptions)
            viewModel.setIsReady(false)
        }
    }

    PlanetScreen(
        planetUiState,
        spacing,
        itemSize,
        stateCardList,
        pagerState,
        lazyListState,
        onMovePlanetPost = {
            viewModel.confirmPostType(it)
        },
        onSetCardId = {
            cardId = it
        }
    )

}

@Composable
internal fun PlanetScreen(
    planetUiState: PlanetUiState,
    spacing: Dp,
    itemSize: Dp,
    cardList: List<UserCard>,
    pagerState: PagerState,
    lazyListState: LazyListState,
    onMovePlanetPost: (String) -> Unit,
    onSetCardId: (String) -> Unit
) {
    Scaffold(
            containerColor = main
        ) { paddingValue ->
        when (planetUiState) {
            PlanetUiState.Error -> ErrorPage("에러")
            PlanetUiState.Loading -> LoadingScreen(text = "로딩")
            is PlanetUiState.Success ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValue)
                        .background(main),
                    state = lazyListState
                ) {
                    item(key = "pager") {
                        PagerSection(pagerState, cardList, onMovePlanetPost, onSetCardId)
                    }

                    item(key = "search") {
                        CompactSearchTextField()
                    }

                    item(key = "toggles") {
                        Toggles(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            text1 = "최신 순",
                            text2 = "참여자 순"
                        )
                    }

                    item(key = "grid") {
                        FlowRowAsGridView(
                            itemSize,
                            spacing,
                            cardList,
                            onMovePlanetPost,
                            onSetCardId
                        )
                    }
                }
        }
    }
}

@Composable
private fun PagerSection(
    pagerState: PagerState,
    imgList: List<UserCard?>,
    onMovePlanetPost: (String) -> Unit,
    onSetCardId: (String) -> Unit
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
            if (page in imgList.indices) {
                val pageOffset by remember(page, pagerState) {
                    derivedStateOf {
                        (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                    }
                }
                PagerCard(
                    pageOffset = pageOffset,
                    card = imgList[page],
                    onMovePlanetPost = onMovePlanetPost,
                    onSetCardId = onSetCardId
                )
            }
        }

        PagerIndicator(pagerState)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun PagerCard(
    pageOffset: Float,
    card: UserCard?,
    onMovePlanetPost: (String) -> Unit,
    onSetCardId: (String) -> Unit
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
            .clickable {
                onMovePlanetPost(card?.userId.toString())
                onSetCardId(card?.cid.toString())
            }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            GlideImage(
                model = card?.image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            GlideImage(
                model = card?.ownerProfile ?: Icons.Filled.AccountCircle,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.BottomCenter)
                    .offset(y = (-16).dp)
                    .clip(CircleShape),
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
    spacing: Dp,
    cardList: List<UserCard>,
    onMovePlanetPost: (String) -> Unit,
    onSetCardId: (String) -> Unit
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spacing),
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        cardList.map {
            PlanetCard(itemSize, it, onMovePlanetPost, onSetCardId)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PlanetCard(
    itemSize: Dp,
    card: UserCard,
    onMovePlanetPost: (String) -> Unit,
    onSetCardId: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .size(itemSize)
            .clickable {
                onMovePlanetPost(card.userId.toString())
                onSetCardId(card.cid.toString())
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            GlideImage(
                model = card.image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            NameSheet(
                Modifier
                    .background(Color.Black.copy(alpha = 0.5f)),
                text = "${card.ownerName}님의 키워드 : ${card.keyWord}",
                alignment = Alignment.TopCenter
            )

            NameSheet(
                Modifier
                    .background(Color.Black.copy(alpha = 0.5f)),
                text = "${card.participatePeople}명 참여중",
                alignment = Alignment.BottomCenter
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NameSheet(
    modifier: Modifier,
    text: String,
    alignment: Alignment
) {
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
                GlideImage(
                    model = R.drawable.ic_peoples,
                    contentDescription = null,
                    modifier = Modifier
                        .size(16.dp)
                )
            }
            Spacer(modifier = Modifier.padding(4.dp))
            // 텍스트 표시
            Text(
                text = text,
                color = Color.White,
                style = customPlanetItemText
            )
        }
    }
}