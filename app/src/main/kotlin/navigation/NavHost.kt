package navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.example.login.LoginRoute
import com.example.login.loginScreen
import com.example.planet.navigation.makePlanetScreen
import com.example.myaccount.myAccountScreen
import com.example.mypage.myPageScreen
import com.example.planet.navigation.PlanetRoute
import com.example.planet.navigation.planetPostScreen
import com.example.planet.navigation.planetScreen
import com.example.planet.navigation.previewScreen
import com.example.rule.ruleScreen
import ui.MainAppState
import ui.mainScreen

@Composable
fun AppNavHost(
    appState: MainAppState,
    modifier: Modifier = Modifier
) {
    val navController = appState.navController
    val viewModel = appState.loginViewModel
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val startDestination : Any = if (isLoggedIn) {
        PlanetRoute
    } else LoginRoute

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth }, // 오른쪽에서 들어오기
                animationSpec = tween(1000)
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth }, // 왼쪽으로 나가기
                animationSpec = tween(1000)
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth }, // 왼쪽에서 들어오기 (뒤로 가기 시 이전 화면)
                animationSpec = tween(1000)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth }, // 오른쪽으로 나가기 (뒤로 가기 시 현재 화면 걷어내기)
                animationSpec = tween(1000)
            )
        }

    ) {
        // Login 화면
        loginScreen(navController, appState.loginViewModel)

        // Main 화면
        mainScreen(appState)

        // Planet 화면
        planetScreen(navController, appState.planetViewModel)

        previewScreen(navController, appState.planetViewModel)

        // MyPage 화면
        myPageScreen(navController)

        // Rule 화면
        ruleScreen(navController)

        // MyAccount 화면
        myAccountScreen(navController)

        makePlanetScreen(navController, appState.makePlanetViewModel)

        planetPostScreen(navController)

    }
}
