package ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.util.trace
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.login.LoginViewModel
import com.example.login.navigateToLogin
import com.example.planet.viewmodel.MakePlanetViewModel
import com.example.mypage.navigateToMyPage
import com.example.planet.viewmodel.PlanetViewModel
import com.example.planet.navigation.navigateToPlanet
import navigation.NavigationDestination

@Composable
fun rememberMainAppState(
    navController: NavHostController = rememberNavController(),
    loginViewModel: LoginViewModel,
    planetViewModel: PlanetViewModel,
    makePlanetViewModel: MakePlanetViewModel
): MainAppState {
    return remember(navController, loginViewModel, planetViewModel, makePlanetViewModel) {
        MainAppState(navController, loginViewModel, planetViewModel, makePlanetViewModel)
    }
}

@Stable
class MainAppState(
    val navController: NavHostController,
    val loginViewModel: LoginViewModel,
    val planetViewModel: PlanetViewModel,
    val makePlanetViewModel: MakePlanetViewModel
) {
    val currentDestination: NavDestination?
        @Composable get() {
            val entry = navController.currentBackStackEntryAsState().value
            return entry?.destination
        }

    val currentTopLevelDestination: NavigationDestination?
        @Composable get() {
            val currentRoute = currentDestination?.route
            val topLevelDestination =
                NavigationDestination.entries.firstOrNull { topLevelDestination ->
                    val match = currentRoute == topLevelDestination.route.qualifiedName
                    match
                }
            return topLevelDestination
        }

    val navigationDestinations: List<NavigationDestination> = NavigationDestination.entries


    fun navigateToTopLevelDestination(navigationDestination: NavigationDestination) {
        trace("Navigation: ${navigationDestination.name}") {
            val topLevelNavOptions = navOptions {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }

            when (navigationDestination) {
                NavigationDestination.Planet -> navController.navigateToPlanet(topLevelNavOptions)
                NavigationDestination.Login -> navController.navigateToLogin(topLevelNavOptions)
                NavigationDestination.MyPage -> navController.navigateToMyPage(topLevelNavOptions)
                NavigationDestination.Main -> navController.navigateToMain(topLevelNavOptions)
            }
        }
    }
}


