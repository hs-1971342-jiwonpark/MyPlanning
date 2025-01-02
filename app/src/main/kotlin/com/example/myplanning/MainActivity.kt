package com.example.myplanning

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.designsystem.theme.MyPlanningTheme
import com.example.login.LoginRoute
import dagger.hilt.android.AndroidEntryPoint
import navigation.AppNavHost
import ui.MainViewModel
import ui.rememberMainAppState

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val appState = rememberMainAppState()
            val mainViewModel : MainViewModel = hiltViewModel()
            MyPlanningTheme {
                AppNavHost(
                    appState = appState,
                    startDestination = LoginRoute::class,
                    context = this
                )
            }
        }
    }
}

