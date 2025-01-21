package ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import kotlinx.serialization.Serializable

@Serializable
data object MainRoute

private const val MAIN_ROUTE = "main"
private const val DEEP_LINK_URI = "myapp://main"

fun NavController.navigateToMain(navOptions: NavOptions) {
    navigate(route = MainRoute, navOptions)
}


fun NavGraphBuilder.mainScreen(appState: MainAppState) {
    composable<MainRoute>(
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "${DEEP_LINK_URI}/${MAIN_ROUTE}"
            }
        )
    ) {
        MainApp(appState)
    }
}


