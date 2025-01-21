package com.example.myplanning

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.designsystem.theme.MyPlanningTheme
import com.example.login.LoginViewModel
import com.example.planet.viewmodel.MakePlanetViewModel
import com.example.planet.viewmodel.PlanetViewModel
import dagger.hilt.android.AndroidEntryPoint
import ui.MainApp
import ui.rememberMainAppState

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // ViewModel 가져오기
            val loginViewModel: LoginViewModel = hiltViewModel()
            val planetViewModel: PlanetViewModel = hiltViewModel()
            val makePlanetViewModel : MakePlanetViewModel = hiltViewModel()
            // MainAppState 초기화
            val appState = rememberMainAppState(
                loginViewModel = loginViewModel,
                planetViewModel = planetViewModel,
                makePlanetViewModel = makePlanetViewModel
            )

            MyPlanningTheme {
                MainApp(appState)
            }

        }
    }
}

