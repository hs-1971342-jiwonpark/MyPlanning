package com.example.myplanning

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.designsystem.theme.MyPlanningTheme
import com.example.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import di.DefaultNavigator
import ui.MainApp
import ui.rememberMainAppState
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var defaultNavigator: DefaultNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // ViewModel 가져오기
            val loginViewModel: LoginViewModel = hiltViewModel()
            // MainAppState 초기화
            val appState = rememberMainAppState(
                loginViewModel = loginViewModel,
                defaultNavigator = defaultNavigator
            )

            MyPlanningTheme {
                BackOnPressed(loginViewModel)
                MainApp(appState)
            }

        }
    }
}

@Composable
fun BackOnPressed(loginViewModel: LoginViewModel) {
    val context = LocalContext.current
    var backPressedTime = 0L // 뒤로가기 버튼을 눌렀던 시간을 저장하는 변수

    BackHandler(enabled = true) {
        // 만약 전에 뒤로가기 버튼 누른 시간과 특정한 시간 만큼 차이가 나지 않으면 앱종료.
        if (System.currentTimeMillis() - backPressedTime <= 1000L) {
            loginViewModel.logOut()
            (context as Activity).finish() // 앱 종료
        } else {
            // 특정한 시간 이상으로 차이가 난다면 토스트로 한 번 더 버튼을 누르라고 알림
            Toast.makeText(context, "한 번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
        // 뒤로가기 버튼을 눌렀던 시간을 저장
        backPressedTime = System.currentTimeMillis()
    }
}
