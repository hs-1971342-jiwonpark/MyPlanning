package ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import com.example.login.LoginScreen
import com.example.login.uri
import com.example.navigation.Dest
import com.example.navigation.FeatureGraph
import com.example.navigation.NavigationDest
import kotlinx.serialization.Serializable

private const val MAIN_ROUTE = "main"
private const val DEEP_LINK_URI = "myapp://main"


interface MainFeature : FeatureGraph

class MainFeatureImpl : MainFeature {
    override fun navGraph(
        navHostController: NavHostController,
        navGraphBuilder: NavGraphBuilder,
        provide : Any?
    ) {
        navGraphBuilder.navigation<NavigationDest.MainRoute>(startDestination = Dest.MainRoute) {
            composable<Dest.MainRoute>(
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern = "${DEEP_LINK_URI}/${MAIN_ROUTE}"
                    }
                )
            ) {
                provide as MainAppState
                MainApp(provide)
            }
        }
    }

}


