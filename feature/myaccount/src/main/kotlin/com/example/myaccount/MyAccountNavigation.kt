package com.example.myaccount

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import kotlinx.serialization.Serializable

@Serializable
data object MyAccountRoute

private const val MY_ACCOUNT_ROUTE = "myAccount"
private const val DEEP_LINK_URI = "myapp://main"

fun NavController.navigateToMyAccount(navOptions: NavOptions? = null) {
    val deepLinkUri = "$DEEP_LINK_URI/$MY_ACCOUNT_ROUTE"
    navigate(deepLinkUri, navOptions)
}

fun NavGraphBuilder.myAccountScreen(navController: NavController) {
    composable(
        route = MY_ACCOUNT_ROUTE,  // 단순 route
        deepLinks = listOf(
            navDeepLink {
                uriPattern = DEEP_LINK_URI  // 전체 딥링크 패턴
            }
        )
    ) {
        MyAccountScreen(navController)
    }
}



