package com.example.planet.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.example.planet.ui.PostType
import com.example.planet.ui.PreviewScreen
import com.example.planet.viewmodel.PlanetViewModel
import kotlinx.serialization.Serializable

@Serializable
data class PreviewRoute(
    val initialCardId: String = "",
    val initialPostType: PostType = PostType.NOT
)

fun NavController.navigateToPreview(
    postType: PostType,
    cardNum: String,
    navOptions: NavOptions
) {
    navigate(
        route = PreviewRoute(
            initialCardId = cardNum,
            initialPostType = postType
        ),
        navOptions = navOptions
    )
}

fun NavGraphBuilder.previewScreen(
    navController: NavController,
    planetViewModel: PlanetViewModel
) {
    composable<PreviewRoute>(
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "$uri/preview"
            }
        )
    ) {
        PreviewScreen(
            navController = navController
        )
    }
}