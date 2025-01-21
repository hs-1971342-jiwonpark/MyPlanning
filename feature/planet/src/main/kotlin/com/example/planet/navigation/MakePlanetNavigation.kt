package com.example.planet.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.example.planet.ui.MakePlanetScreen
import com.example.planet.viewmodel.MakePlanetViewModel
import kotlinx.serialization.Serializable

@Serializable
data object MakePlanetNavigation

private const val DEEP_LINK_URI = "myapp://main"

fun NavController.navigateToMakePlanet(navOptions: NavOptions) {
    navigate(MakePlanetNavigation, navOptions)
}

fun NavGraphBuilder.makePlanetScreen(navController: NavController, makePlanetViewModel: MakePlanetViewModel) {
    composable<MakePlanetNavigation>(
        deepLinks = listOf(
            navDeepLink {
                uriPattern = DEEP_LINK_URI  // 전체 딥링크 패턴
            }
        )
    ) {
        MakePlanetScreen(navController = navController, viewModel = makePlanetViewModel)
    }
}



