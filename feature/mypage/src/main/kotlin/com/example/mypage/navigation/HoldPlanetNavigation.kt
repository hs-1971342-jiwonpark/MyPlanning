package com.example.mypage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import com.example.mypage.ui.HoldPlanetScreen
import com.example.navigation.Dest
import com.example.navigation.FeatureGraph
import com.example.navigation.NavigationDest

interface HoldPlanetFeature : FeatureGraph

class HoldPlanetFeatureImpl : HoldPlanetFeature {
    override fun navGraph(
        navHostController: NavHostController,
        navGraphBuilder: NavGraphBuilder,
        provide: Any?
    ) {
        navGraphBuilder.navigation<NavigationDest.HoldPlanetRoute>(startDestination = Dest.HoldPlanetRoute) {
            composable<Dest.HoldPlanetRoute>(
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern = "$uri/holdPlanet"
                    }
                )
            ) {
                HoldPlanetScreen(navHostController)
            }
        }
    }

}


