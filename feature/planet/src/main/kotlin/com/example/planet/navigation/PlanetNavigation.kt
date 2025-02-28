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
import com.example.planet.ui.PlanetScreen

val uri = "myapp://main"

interface PlanetFeature : FeatureGraph

class PlanetFeatureImpl : PlanetFeature {
    override fun navGraph(
        navHostController: NavHostController,
        navGraphBuilder: NavGraphBuilder,
        provide: Any?
    ) {
        navGraphBuilder.navigation<NavigationDest.PlanetRoute>(startDestination = Dest.PlanetRoute) {
            composable<Dest.PlanetRoute>(
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern = "$uri/planet"
                    }
                )
            ) {
                PlanetScreen(navHostController)
            }
        }
    }

}
