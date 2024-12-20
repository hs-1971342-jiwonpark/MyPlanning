package com.example.login

import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import kotlinx.serialization.Serializable

@Serializable
data object LoginRoute

val uri = "myapp://main"

fun NavController.navigateToLogin(navOptions: NavOptions) {
    navigate(LoginRoute, navOptions)
}

fun NavGraphBuilder.loginScreen(
    navController: NavController,
    context: Context
) {
    composable<LoginRoute>(
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "$uri/login"
            }
        )
    ) {
        LoginScreen(
            context = context, navController = navController
        )
    }
}


