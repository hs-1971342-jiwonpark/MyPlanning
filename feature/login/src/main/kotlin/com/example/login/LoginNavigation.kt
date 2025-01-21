package com.example.login

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
    navigate(LoginRoute,navOptions)
}

fun NavGraphBuilder.loginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel
) {
    composable<LoginRoute>(
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "$uri/login"
            }
        )
    ) {
        LoginScreen(
            loginViewModel = loginViewModel
        )
    }
}


