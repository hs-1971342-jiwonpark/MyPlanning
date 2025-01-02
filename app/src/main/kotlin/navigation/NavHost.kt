package navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.login.loginScreen
import com.example.myaccount.myAccountScreen
import com.example.mypage.myPageScreen
import com.example.planet.navigation.planetScreen
import com.example.rule.ruleScreen
import ui.MainApp
import ui.MainAppState
import ui.mainScreen
import kotlin.reflect.KClass

@Composable
fun AppNavHost(
    context: Context,
    appState: MainAppState,
    modifier: Modifier = Modifier,
    startDestination: KClass<*>
) {
    NavHost(
        navController = appState.navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // 로그인 화면
        loginScreen(
            navController = appState.navController
        )
        // 메인 화면
        mainScreen()
        // 로그인 -> 메인
        composable("main") { MainApp() }
        // Planet 화면
        planetScreen()
        // MyPage 화면
        myPageScreen(navController = appState.navController)
        myAccountScreen(navController = appState.navController)
        ruleScreen(navController = appState.navController)
    }
}