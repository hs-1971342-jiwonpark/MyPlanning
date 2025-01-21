package com.example.rule

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import kotlinx.serialization.Serializable

@Serializable
data object RuleRoute

private const val MY_RULE_ROUTE = "rule"
private const val DEEP_LINK_URI = "myapp://main"

fun NavGraphBuilder.ruleScreen(navController: NavController) {
    composable(

        route = MY_RULE_ROUTE,  // 단순 route
        deepLinks = listOf(
            navDeepLink {
                uriPattern = DEEP_LINK_URI  // 전체 딥링크 패턴
            }
        )
    ) {
        RuleScreen(navController)
    }
}


