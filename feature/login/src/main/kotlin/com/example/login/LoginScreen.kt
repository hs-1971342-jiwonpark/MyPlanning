package com.example.login

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.data.model.LoginState
import com.example.designsystem.component.text.LoginText
import com.example.designsystem.theme.MyPlanningTheme
import com.example.designsystem.theme.main
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
                if (loginState is LoginState.Success)
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
    onLoginSuccess: () -> Unit
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
        Row {
            Image(
                painter = painterResource(id = R.drawable.android_light_rd_na),
                contentScale = ContentScale.Fit,
                contentDescription = null,
                modifier = Modifier.clickable { onLoginClick() }.padding(4.dp)
            )
            Spacer(Modifier.size(20.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_kakao_btn),
                contentScale = ContentScale.Fit,
                contentDescription = null,
                modifier = Modifier.clickable { onLoginClickForKakao() }.padding(4.dp)
            )
        }
        Spacer(Modifier.height(40.dp))

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


@Composable
fun CustomGoogleSignInButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .height(48.dp)
            .width(312.dp)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Google 로고
        Image(
            painter = painterResource(id = R.drawable.android_light_rd_na), // Google에서 제공하는 로고 사용
            contentDescription = "Google Logo",
            modifier = Modifier.size(44.dp)
        )
        Spacer(Modifier.width(12.dp))

        // Google 로그인 텍스트
        Text(
            text = "Google 계정으로 로그인",
            color = Color.Black,
            fontFamily = FontFamily()
        )
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
