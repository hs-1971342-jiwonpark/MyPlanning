package com.example.myaccount

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.data.model.User
import com.example.designsystem.theme.ProgressBackGroundColor
import com.example.designsystem.theme.ProgressBarColor
import com.example.designsystem.theme.WhiteAlpha65
import com.example.designsystem.theme.main

@Composable
internal fun MyAccountScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: MyAccountViewModel = hiltViewModel()
) {
    val userPrefData by viewModel.userPrefData.collectAsState()
    var isVisible by remember { mutableStateOf(false) }
    val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }

    LaunchedEffect(userPrefData) {
        isVisible = true
    }

    MyAccountScreen(
        bitmap = bitmap,
        navController = navController,
        modifier = modifier,
        user = userPrefData,
        isVisible = isVisible
    )
}

@Composable
internal fun MyAccountScreen(
    bitmap: MutableState<Bitmap?>,
    navController: NavController,
    modifier: Modifier = Modifier,
    user: User,
    isVisible: Boolean
) {
    val exp = user.exp.toFloat()
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        containerColor = main
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            MainProfile(
                profileUrl = user.photoUrl,
                name = user.name,
                bitmap = bitmap
            )
            CardList(exp, isVisible)
        }
    }
}


@Composable
fun MainProfile(
    profileUrl: String,
    name: String,
    bitmap: MutableState<Bitmap?>
) {
    Column(
        modifier = Modifier
            .padding(top = 40.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Glide.with(LocalContext.current)
            .asBitmap()
            .load(profileUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(p0: Bitmap, p1: Transition<in Bitmap>?) {
                    bitmap.value = p0
                }

                override fun onLoadCleared(p0: Drawable?) {
                }

            })

        val modifierImage = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(Color.Gray)

        bitmap.value?.asImageBitmap()?.let { fetchBitmap ->
            Image(
                bitmap = fetchBitmap,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = modifierImage
            )
        } ?: Image(
            painter = painterResource(id = R.drawable.ic_peoples),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = modifierImage
        )

        Spacer(modifier = Modifier.height(16.dp)) // 16.dp 높이 간격

        Text(
            text = name,
            color = Color.White
        )
    }
}

@Composable
fun CardList(
    progress: Float,
    isVisible: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        MenuCard("레벨 및 경험치", progress, isVisible)
    }
}

@Composable
fun MenuCard(
    menuName: String,
    progress: Float,
    isVisible: Boolean
) {
    var startAnimation by remember { mutableStateOf(false) }

    val progressAnimation by animateFloatAsState(
        targetValue = if (startAnimation) (progress % 100f) / 100f else 0f,
        animationSpec = tween(
            durationMillis = 1500,
            easing = FastOutSlowInEasing
        ),
        label = ""
    )

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 0.5.dp, color = Color.Black)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                color = WhiteAlpha65,
                style = MaterialTheme.typography.labelMedium,
                text = menuName,
                modifier = Modifier.weight(0.25f),
                textAlign = TextAlign.Center
            )
            Column(
                modifier = Modifier
                    .weight(0.7f)
            ) {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn() + expandVertically()
                ) {
                    Box(
                        contentAlignment = Alignment.Center // Box 내부에서 텍스트와 프로그래스바를 정렬
                    ) {
                        LinearProgressIndicator(
                            drawStopIndicator = {},
                            progress = { progressAnimation },
                            color = ProgressBarColor,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 16.dp)
                                .height(20.dp),
                            trackColor = ProgressBackGroundColor,
                            gapSize = (-15).dp,
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = "LV${progress / 100} ${progress % 100f}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = main,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun PrevMenuCard() {
    CardList(4820.0f, true)
}