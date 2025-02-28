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
import com.example.navigation.NavigationDest
import di.DefaultNavigator
import navigation.NavigationDestination

@Composable
fun rememberMainAppState(
    navController: NavHostController = rememberNavController(),
    loginViewModel: LoginViewModel,
    defaultNavigator: DefaultNavigator
): MainAppState {
    return remember(navController, loginViewModel, defaultNavigator) {
        MainAppState(navController, loginViewModel, defaultNavigator)
    }
}

@Stable
class MainAppState(
    val navController: NavHostController,
    val loginViewModel: LoginViewModel,
    val defaultNavigator: DefaultNavigator
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
                    saveState = false
                }
                launchSingleTop = true
                restoreState = true
            }

            when (navigationDestination) {
                NavigationDestination.Planet -> navController.navigate(
                    NavigationDest.PlanetRoute,
                    topLevelNavOptions
                )

                NavigationDestination.Login -> navController.navigate(
                    NavigationDest.LoginRoute,
                    topLevelNavOptions
                )

                NavigationDestination.MyPage -> navController.navigate(
                    NavigationDest.MyPageRoute,
                    topLevelNavOptions
                )

                NavigationDestination.Main -> navController.navigate(
                    NavigationDest.MainRoute,
                    topLevelNavOptions
                )
            }
        }
    }
}


