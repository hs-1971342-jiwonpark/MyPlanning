package com.example.mypage

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import kotlinx.serialization.Serializable

@Serializable
data object MyPageRoute {
    val list = listOf(
        "MyAccount", "Rule"
    )
}

const val uri = "myapp://main"

fun NavController.navigateToMyPage(navOptions: NavOptions) {
    navigate(MyPageRoute, navOptions)
}

fun NavGraphBuilder.myPageScreen(navController: NavController) {
    composable<MyPageRoute>(
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "$uri/myPage"
            }
        )
    ) {
        MyPageScreen(navController)
    }
}


