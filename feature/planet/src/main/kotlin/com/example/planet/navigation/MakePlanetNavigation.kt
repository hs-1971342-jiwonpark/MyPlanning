package com.example.planet.navigation

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
import com.example.planet.ui.MakePlanetScreen


private const val DEEP_LINK_URI = "myapp://main"
private const val MY_ACCOUNT_ROUTE = "myAccount"

interface MakePlanetFeature : FeatureGraph

class MakePlanetFeatureImpl : MakePlanetFeature {
    override fun navGraph(
        navHostController: NavHostController,
        navGraphBuilder: NavGraphBuilder,
        provide: Any?
    ) {
        navGraphBuilder.navigation<NavigationDest.MakePlanetNavigation>(startDestination = Dest.MakePlanetNavigation) {
            composable<Dest.MakePlanetNavigation>(
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern = DEEP_LINK_URI  // 전체 딥링크 패턴
                    }
                )
            ) {
                MakePlanetScreen(navController = navHostController)
            }
        }
    }
}







