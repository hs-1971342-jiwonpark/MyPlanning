package com.example.planet.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import kotlinx.serialization.Serializable

@Serializable
data object PlanetRoute

val uri = "myapp://main"

fun NavController.navigateToPlanet(navOptions: NavOptions) {
    navigate(PlanetRoute, navOptions)
}

fun NavGraphBuilder.planetScreen(
) {
    composable<PlanetRoute>(
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "$uri/planet"
            }
        )
    ) {
        PlanetScreen()
    }
}


