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
import com.example.planet.ui.PlanetPostScreen

fun NavController.navigateToPlanetPost(cid: String, navOptions: NavOptions) {
    navigate(NavigationDest.PlanetPostRoute(cid), navOptions)
}

interface PlanetPostFeature : FeatureGraph

class PlanetPostFeatureImpl : PlanetPostFeature {
    override fun navGraph(
        navHostController: NavHostController,
        navGraphBuilder: NavGraphBuilder,
        provide: Any?
    ) {
        navGraphBuilder.navigation<NavigationDest.PlanetPostRoute>(
            startDestination = Dest.PlanetPostRoute(
                cid = ""
            )
        ) {
            composable<Dest.PlanetPostRoute>(
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern = "$uri/planetPost"
                    }
                )
            ) {
                PlanetPostScreen(navController = navHostController)
            }
        }
    }
}








