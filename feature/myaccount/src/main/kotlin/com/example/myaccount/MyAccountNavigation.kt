package com.example.myaccount

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import kotlinx.serialization.Serializable
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import com.example.navigation.Dest
import com.example.navigation.FeatureGraph
import com.example.navigation.NavigationDest


private const val MY_ACCOUNT_ROUTE = "myAccount"
private const val DEEP_LINK_URI = "myapp://main"


fun NavController.navigateToMyAccount(navOptions: NavOptions? = null) {
    val deepLinkUri = "$DEEP_LINK_URI/$MY_ACCOUNT_ROUTE"
    navigate(deepLinkUri, navOptions)
}

interface MyAccountFeature : FeatureGraph

class MyAccountFeatureImpl : MyAccountFeature {
    override fun navGraph(
        navHostController: NavHostController,
        navGraphBuilder: NavGraphBuilder,
        provide: Any?
    ) {
        navGraphBuilder.navigation<NavigationDest.MyAccountRoute>(startDestination = Dest.MyAccountRoute) {
            composable<Dest.MyAccountRoute>(
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern = DEEP_LINK_URI  // 전체 딥링크 패턴
                    }
                )
            ) {
                MyAccountScreen(navHostController)
            }
        }
    }
}



