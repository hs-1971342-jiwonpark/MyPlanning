package com.example.planet.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.data.model.UserCard
import com.example.designsystem.component.text.ErrorPage
import com.example.designsystem.component.text.LoadingScreen
import com.example.designsystem.component.text.Toggles
import com.example.designsystem.theme.SubMain
import com.example.designsystem.theme.main
import com.example.planet.R
import com.example.planet.viewmodel.PlanetPostUiState
import com.example.planet.viewmodel.PlanetPostViewModel

@Composable
internal fun PlanetPostScreen(
    viewModel: PlanetPostViewModel = hiltViewModel(),
    navController: NavController
) {
    val planetPostUiState by viewModel.planetPostUiState.collectAsState()
    PlanetPostScreen(
        uiState = planetPostUiState,
        onClick = {}
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
internal fun PlanetPostScreen(
    uiState: PlanetPostUiState,
    onClick: () -> Unit
) {
    when (uiState) {
        PlanetPostUiState.Error -> ErrorPage("에러")
        PlanetPostUiState.Loading -> LoadingScreen(text = "로딩")
        is PlanetPostUiState.Success -> Scaffold(
            containerColor = main,
            contentColor = Color.White,
            floatingActionButton = {
                AddPostFloatingButton(
                    onClick = onClick
                )
            }

        ) { paddingValue ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValue)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Row(
                        Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = uiState.card.keyWord + "행성에 오신 것을 환영합니다!!",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                }
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        GlideImage(
                            modifier = Modifier.clip(CircleShape),
                            model = uiState.card.ownerProfile,
                            contentDescription = null
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = uiState.card.ownerName,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
                item {
                    GlideImage(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .height(400.dp),
                        model = uiState.card.image,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
                item {
                    Text(
                        modifier = Modifier
                            .background(SubMain)
                            .fillParentMaxWidth()
                            .padding(5.dp),
                        text = uiState.card.description,
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                item {
                    Toggles(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp),
                        text1 = "최신 순",
                        text2 = "참여자 순"
                    )
                }

                items(50) { index ->
                    ContentCard()
                }
            }
        }
    }

}


@Composable
fun AddPostFloatingButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = { onClick() },
        shape = CircleShape,
        containerColor = SubMain,
        contentColor = Color.White
    ) {
        Icon(Icons.Filled.Add, "Floating action button.")
    }
}


@Preview
@Composable
fun d() {
    PlanetPostScreen(
        uiState = PlanetPostUiState.Success(
            card = UserCard()
        ),
        onClick = { }
    )
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ContentCard() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            GlideImage(
                modifier = Modifier.clip(CircleShape),
                model = R.drawable.ic_add_image,
                contentDescription = null
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = "사용자",
                style = MaterialTheme.typography.labelLarge
            )
        }
        GlideImage(
            modifier = Modifier
                .clip(CircleShape)
                .fillMaxWidth()
                .aspectRatio(1f),
            model = R.drawable.ic_add_image,
            contentDescription = null
        )
    }
}