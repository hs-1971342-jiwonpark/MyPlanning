package com.example.planet.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.example.planet.ui.EdiPostPlanetScreen
import com.example.planet.viewmodel.PlanetViewModel
import com.example.planet.ui.PlanetScreen
import kotlinx.serialization.Serializable

@Serializable
data object EditPlanetPostRoute

fun NavController.navigateToEditPostPlanet(navOptions: NavOptions) {
    navigate(EditPlanetPostRoute, navOptions)
}

fun NavGraphBuilder.editPostPlanetScreen(
    navController: NavController,
    planetViewModel: PlanetViewModel
) {
    composable<EditPlanetPostRoute>(
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "$uri/editPlanetPost"
            }
        )
    ) {
        EdiPostPlanetScreen(navController =  navController)
    }
}


