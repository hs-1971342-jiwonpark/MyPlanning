package navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.example.navigation.NavigationDest
import ui.MainAppState


@Composable
fun AppNavHost(
    appState: MainAppState,
    modifier: Modifier = Modifier
) {
    val navController = appState.navController
    val defaultNavigator = appState.defaultNavigator
    val viewModel = appState.loginViewModel
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    val destination = if (isLoggedIn) {
        NavigationDest.PlanetRoute
    } else {
        NavigationDest.LoginRoute
    }

    NavHost(
        navController = navController,
        startDestination = destination,
        modifier = modifier,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(500)
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(500)
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(500)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(500)
            )
        }

    ) {

        defaultNavigator.logInFeature.navGraph(navController, this, appState.loginViewModel)

        defaultNavigator.mainFeature.navGraph(navController, this, appState)

        defaultNavigator.planetFeature.navGraph(navController, this, null)

        defaultNavigator.previewFeature.navGraph(navController, this, null)

        defaultNavigator.myPageFeature.navGraph(navController, this, null)

        defaultNavigator.holdPlanetFeature.navGraph(navController, this, null)

        defaultNavigator.ruleFeature.navGraph(navController, this, null)

        defaultNavigator.myAccountFeature.navGraph(navController, this, null)

        defaultNavigator.makePlanetFeature.navGraph(navController, this, null)

        defaultNavigator.planetPostFeature.navGraph(navController, this, null)
    }
}
