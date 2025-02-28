package com.example.rule

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import com.example.navigation.Dest
import com.example.navigation.FeatureGraph
import com.example.navigation.NavigationDest


private const val MY_RULE_ROUTE = "rule"
private const val DEEP_LINK_URI = "myapp://main"


fun NavController.navigateToRule(navOptions: NavOptions) {
    navigate(NavigationDest.RuleRoute, navOptions)
}

interface RuleFeature : FeatureGraph

class RuleFeatureImpl : RuleFeature {
    override fun navGraph(
        navHostController: NavHostController,
        navGraphBuilder: NavGraphBuilder,
        provide: Any?
    ) {
        navGraphBuilder.navigation<NavigationDest.RuleRoute>(startDestination = Dest.RuleRoute) {
            composable<Dest.RuleRoute>(
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern = DEEP_LINK_URI  // 전체 딥링크 패턴
                    }
                )
            ) {
                RuleScreen(navHostController)
            }
        }
    }

}



