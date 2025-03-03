package com.example.planet.ui

import android.view.ViewTreeObserver
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
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
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
import com.example.navigation.Dest
import com.example.planet.viewmodel.PlanetUiState
import com.example.planet.viewmodel.PlanetViewModel
import kotlin.math.absoluteValue

@Composable
fun keyboardHeightObserver(): Dp {
    val view = LocalView.current
    val density = LocalDensity.current
    var keyboardHeight by remember { mutableStateOf(0.dp) }

    DisposableEffect(view) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = android.graphics.Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.height
            val keypadHeight = screenHeight - rect.bottom

            keyboardHeight = if (keypadHeight > screenHeight * 0.15) {
                with(density) { keypadHeight.toDp() }
            } else {
                0.dp
            }
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }

    return keyboardHeight
}

@Composable
internal fun PlanetScreen(
    navController: NavController,
    viewModel: PlanetViewModel = hiltViewModel()
) {
    val keyBoardHeight = keyboardHeightObserver()
    var searchText by remember { mutableStateOf("") }
    var cardId by rememberSaveable { mutableStateOf("") }
    val configuration = LocalConfiguration.current
    val columns = 2
    val spacing = 16.dp
    val itemSize by remember(configuration.screenWidthDp) {
        derivedStateOf {
            (configuration.screenWidthDp.dp - ((columns + 1) * spacing)) / columns
        }
    }
    val lazyListState = rememberLazyListState()
    val planetUiState by viewModel.planetUiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchCardListData()
    }

    PlanetScreen(
        keyBoardHeight,
        searchText,
        { newText ->
            searchText = newText
            viewModel.filteredCard(newText)
        },
        { viewModel.sortedByPopular() },
        { viewModel.sortedByRecent() },
        planetUiState,
        spacing,
        itemSize,
        lazyListState,
        onMovePlanetPost = { card ->
            cardId = card.cid.toString()
            viewModel.confirmPostType(card.userId.toString()) {
                navController.navigate(
                    Dest.PreviewRoute(
                        initialPostType = viewModel.postType.value,
                        initialCardId = cardId
                    )
                )
            }
        },
        onSetCardId = {
            cardId = it
        }
    )

}

@OptIn(ExperimentalLayoutApi::class, ExperimentalGlideComposeApi::class)
@Composable
internal fun PlanetScreen(
    keyBoardHeight: Dp,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSortedByPopular: () -> Unit,
    onSortedByRecent: () -> Unit,
    planetUiState: PlanetUiState,
    spacing: Dp,
    itemSize: Dp,
    lazyListState: LazyListState,
    onMovePlanetPost: (UserCard) -> Unit,
    onSetCardId: (String) -> Unit
) {
    val paddings = if (keyBoardHeight > 0.dp) {
        keyBoardHeight
    } else {
        0.dp
    }

    when (planetUiState) {
        PlanetUiState.Error -> ErrorPage("에러")
        PlanetUiState.Loading -> LoadingScreen(text = "로딩")
        is PlanetUiState.Success ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(main)
                    .imePadding(),
                state = lazyListState
            ) {
                item(key = "pager") {
                    val pagerState = rememberPagerState { planetUiState.pageData.size }
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(spacing),
                        text = "지금 뜨는 행성",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        HorizontalPager(
                            state = pagerState,
                            contentPadding = PaddingValues(horizontal = 48.dp)
                        ) { page ->
                            if (page in planetUiState.pageData.indices) {
                                val pageOffset by remember(page, pagerState) {
                                    derivedStateOf {
                                        (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                                    }
                                }
                                PagerCard(
                                    pageOffset = pageOffset,
                                    card = planetUiState.pageData[page],
                                    onMovePlanetPost = onMovePlanetPost,
                                    onSetCardId = onSetCardId
                                )
                            }
                        }

                        PagerIndicator(pagerState)
                    }
                }

                item(key = "search") {
                    Text(  modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = spacing, end = spacing, top = 16.dp,bottom = 24.dp),
                        text = "행성 찾아보기",
                        style = MaterialTheme.typography.titleLarge
                    )
                    CompactSearchTextField(searchText, onSearchTextChange)
                }

                item(key = "toggles") {
                    Toggles(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = spacing),
                        text1 = "최신 순",
                        text2 = "참여자 순",
                        clickable1 = onSortedByRecent,
                        clickable2 = onSortedByPopular
                    )
                }

                item(key = "grid") {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = spacing)
                            .padding(bottom = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(spacing),
                        verticalArrangement = Arrangement.spacedBy(spacing)
                    ) {
                        planetUiState.cardList.map { card ->
                            Card(
                                modifier = Modifier
                                    .size(itemSize)
                                    .clickable {
                                        onMovePlanetPost(card)
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
                    }
                }
                item {
                    Spacer(Modifier.height(paddings))
                }
            }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun PagerCard(
    pageOffset: Float,
    card: UserCard?,
    onMovePlanetPost: (UserCard) -> Unit,
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
                if (card != null) {
                    onMovePlanetPost(card)
                    onSetCardId(card.cid.toString())
                }
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

