package com.example.planet.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.data.model.PostType
import com.example.designsystem.component.text.CustomButton
import com.example.designsystem.component.text.ErrorPage
import com.example.designsystem.component.text.LoadingScreen
import com.example.designsystem.theme.main
import com.example.navigation.Dest
import com.example.planet.viewmodel.PreviewUiState
import com.example.planet.viewmodel.PreviewViewModel

@Composable
internal fun PreviewScreen(
    viewModel: PreviewViewModel = hiltViewModel(),
    navController: NavController
) {
    val previewUiState by viewModel.previewUiState.collectAsState()
    val cardNum = viewModel.cId
    val isParticipatedIn by viewModel.isParticipatedIn.collectAsState()

    val onClick: () -> Unit = { navController.navigate(Dest.PlanetPostRoute(cardNum)) }

    PreviewScreen(
        isParticipatedIn = isParticipatedIn,
        backgroundImg = "",
        goNextScreen = {
            viewModel.upDateParticipateInPeople(cardNum)
            onClick()
        },
        previewUiState = previewUiState,
        onClick = onClick
    )

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
internal fun PreviewScreen(
    isParticipatedIn: Boolean,
    previewUiState: PreviewUiState,
    backgroundImg: Any,
    goNextScreen: () -> Unit,
    onClick: () -> Unit
) {
    Scaffold(
        containerColor = main,
        contentColor = Color.White
    ) { paddingValue ->
        when (previewUiState) {
            PreviewUiState.Error -> {
                ErrorPage("오류")
            }

            PreviewUiState.Loading -> {
                LoadingScreen(text = "이미지를 불러오는")
            }

            is PreviewUiState.Success ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValue)
                ) {
                    Box {
                        GlideImage(
                            modifier = Modifier
                                .fillMaxSize(),
                            model = previewUiState.userCard.image,
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.5f))
                        )
                        Column {
                            Spacer(Modifier.height(40.dp))
                            Text(
                                text = previewUiState.userCard.keyWord,
                                style = MaterialTheme.typography.displayLarge,
                                modifier = Modifier
                                    .padding(20.dp)
                            )
                            Spacer(Modifier.height(20.dp))
                            Text(
                                text = previewUiState.userCard.description,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier
                                    .padding(20.dp)
                            )
                        }
                        if (previewUiState.postType == PostType.OTHER && !isParticipatedIn) {
                            CustomButton(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(40.dp),
                                text = "참여하기",
                                onClick = goNextScreen
                            )
                        } else {
                            CustomButton(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(40.dp),
                                text = "이동하기",
                                onClick = onClick
                            )
                        }
                    }
                }

        }

    }
}
