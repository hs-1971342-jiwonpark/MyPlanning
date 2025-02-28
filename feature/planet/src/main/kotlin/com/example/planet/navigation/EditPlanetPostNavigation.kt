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

interface EditPlanetPostFeature : FeatureGraph

class EditPlanetPostFeatureImpl : EditPlanetPostFeature {
    override fun navGraph(
        navHostController: NavHostController,
        navGraphBuilder: NavGraphBuilder,
        provide: Any?
    ) {
        navGraphBuilder.navigation<NavigationDest.EditPlanetPostRoute>(startDestination = Dest.EditPlanetPostRoute) {
            composable<Dest.EditPlanetPostRoute>(
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern = "$uri/editPlanetPost"
                    }
                )
            ) {
                MakePlanetScreen(navController = navHostController)
            }
        }
    }
}








