package com.example.planet.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.example.planet.ui.PlanetPostScreen
import kotlinx.serialization.Serializable

@Serializable
data class PlanetPostRoute(
    val cid : String = ""
)

fun NavController.navigateToPlanetPost(cid : String,navOptions: NavOptions) {
    navigate(PlanetPostRoute(cid), navOptions)
}

fun NavGraphBuilder.planetPostScreen(
    navController: NavController
) {
    composable<PlanetPostRoute>(
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "$uri/planetPost"
            }
        )
    ) {
        PlanetPostScreen(navController = navController)
    }
}


