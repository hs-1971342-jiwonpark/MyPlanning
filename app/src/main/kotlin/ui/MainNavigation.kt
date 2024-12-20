package ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import kotlinx.serialization.Serializable

@Serializable
data object MainRoute

const val uri = "myapp://main"

fun NavController.navigateToMain(navOptions: NavOptions) {
    navigate(MainRoute, navOptions)
}

fun NavGraphBuilder.mainScreen() {
    composable<MainRoute>(
        deepLinks = listOf(
            navDeepLink {
                uriPattern = uri
            }
        )
    ) {
        MainApp()
    }
}


