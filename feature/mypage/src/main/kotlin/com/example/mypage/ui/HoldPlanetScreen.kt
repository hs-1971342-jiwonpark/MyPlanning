package com.example.mypage.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.data.model.UserCard
import com.example.designsystem.R
import com.example.designsystem.component.text.ErrorPage
import com.example.designsystem.component.text.LoadingScreen
import com.example.designsystem.theme.customPlanetItemText
import com.example.utils.Extension
import com.example.mypage.viewmodel.HoldPlanetUiState
import com.example.mypage.viewmodel.HoldPlanetViewModel
import com.example.navigation.Dest

@Composable
internal fun HoldPlanetScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: HoldPlanetViewModel = hiltViewModel()
) {
    val holdPlanetUiState by viewModel.holdUiState.collectAsState()
    var cardId by rememberSaveable { mutableStateOf("") }

    HoldPlanetScreen(
        navController = navController,
        modifier = modifier,
        holdPlanetUiState = holdPlanetUiState,
        onMovePlanetPost = { card ->
            cardId = card.cid.toString()
            viewModel.confirmPostType(card.userId.toString()) {
                navController.navigate(
                    Dest.PreviewRoute(
                        initialCardId = card.cid.toString(),
                        initialPostType = viewModel.postType.value
                    ),
                    Extension.setNavOptions(navController)
                )
            }
        },
        onSetCardId = {
            cardId = it
        }
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
internal fun HoldPlanetScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    holdPlanetUiState: HoldPlanetUiState,
    onMovePlanetPost: (UserCard) -> Unit,
    onSetCardId: (String) -> Unit
) {
    when (holdPlanetUiState) {
        HoldPlanetUiState.Error -> ErrorPage("ERROR")
        HoldPlanetUiState.Loading -> LoadingScreen(text = "보유 행성을 불러오는 중 입니다...")
        is HoldPlanetUiState.Success -> LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp)
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            holdPlanetUiState.cardList.map { card ->
                item {
                    Card(
                        modifier = Modifier
                            .size(300.dp)
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
                    Spacer(Modifier.padding(20.dp))
                }
            }
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

