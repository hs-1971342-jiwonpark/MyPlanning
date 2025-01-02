package com.example.mypage

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.data.model.User
import com.example.designsystem.theme.WhiteAlpha65
import com.example.designsystem.theme.main

@Composable
internal fun MyPageScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: MyPageViewModel = hiltViewModel()
) {
    val userData by viewModel.userData.collectAsState()

    MyPageScreen(
        navController = navController,
        modifier = modifier,
        user = userData ?: User("", "", "h")
    )
}

@Composable
internal fun MyPageScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    user: User
) {

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
                user.photoUrl, user.name
            )
            CardList(navController)
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MainProfile(profileUrl: String, name: String) {
    val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }

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
fun CardList(navController: NavController) {
    val menuList = listOf(
        "내 정보",
        "이용 약관",
        "보유 행성"
    )
    Column {
        menuList.forEachIndexed { index, item ->
            MenuCard(item, navController, index)
        }
    }
}

const val MY_ACCOUNT_ROUTE = "myAccount"
const val MY_RULE_ROUTE = "rule"

@Composable
fun MenuCard(menuName: String, navController: NavController, index: Int) {
    Column(
        modifier = Modifier
            .border(width = 0.5.dp, color = Color.Black)
    ) {
        Button(
            onClick = {
                val navOptions = navOptions {
                    launchSingleTop = true
                }
                when (index) {
                    0 -> navController.navigate(MY_ACCOUNT_ROUTE, navOptions)
                    1 -> navController.navigate(MY_RULE_ROUTE, navOptions)
                }
            },
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

}

