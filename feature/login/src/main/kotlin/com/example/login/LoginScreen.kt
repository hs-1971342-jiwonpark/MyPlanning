package com.example.login

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.data.model.LoginState
import com.example.designsystem.component.text.LoginText
import com.example.designsystem.theme.MyPlanningTheme
import com.example.designsystem.theme.main
import com.example.navigation.Dest
import com.example.navigation.NavigationDest

@Composable
fun LoginScreen(loginViewModel: LoginViewModel, navController: NavController) {

    MyPlanningTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            val viewModel: LoginViewModel = loginViewModel
            val loginState by viewModel.loginState.collectAsState()
            val activity = LocalContext.current as? Activity

            LaunchedEffect(loginState) {
                if(loginState is LoginState.Success)
                    navController.navigate(NavigationDest.PlanetRoute)
            }

            LoginBackground(
                loginState = loginState,
                onLoginClick = { viewModel.performGoogleLogin() },
                onLoginClickForKakao = {
                    activity?.let {
                        viewModel.performKakaoLogin(it)
                    } ?: Log.e("Error", "ERROR: Activity is null.")
                },
                onLoginSuccess = { viewModel.updateLoginState(true) }
            )
        }
    }
}

@Composable
internal fun LoginBackground(
    loginState: LoginState,
    onLoginClick: () -> Unit,
    onLoginClickForKakao: () -> Unit,
    onLoginSuccess : () -> Unit
) {
    Column(
        modifier = Modifier
            .background(main)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_transparent),
            contentScale = ContentScale.Fit,
            contentDescription = null
        )
        Spacer(Modifier.size(150.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_kakao_login),
            contentScale = ContentScale.Fit,
            contentDescription = null,
            modifier = Modifier.clickable {
                onLoginClick() // 버튼 클릭 시 이벤트 처리
            }
        )
        Spacer(Modifier.size(20.dp))

        Image(
            painter = painterResource(id = R.drawable.ic_kakao_login),
            contentScale = ContentScale.Fit,
            contentDescription = null,
            modifier = Modifier.clickable {
                onLoginClickForKakao()
            }
        )
        Spacer(Modifier.size(20.dp))

        when (loginState) {
            is LoginState.Idle -> LoginText("Welcome! Please log in.")
            is LoginState.Loading -> CircularProgressIndicator()
            is LoginState.Success -> {
                val user = loginState.user
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    LoginText("Welcome, ${user.name}님 환영합니다!")
                }
                onLoginSuccess()
            }

            is LoginState.Error -> LoginText("Error: ${loginState.message}")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LoginBackground(
        loginState = LoginState.Idle,
        onLoginClick = {},
        onLoginClickForKakao = {},
        onLoginSuccess = {}
    )
}
