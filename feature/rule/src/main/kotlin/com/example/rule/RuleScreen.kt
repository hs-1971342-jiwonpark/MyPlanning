package com.example.rule

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.designsystem.theme.main

@Composable
internal fun RuleScreen(
    navController: NavController
) {

    RuleScreen(
        navController = navController,
        modifier = Modifier
    )
}

@Composable
internal fun RuleScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        containerColor = main
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CardList(navController)
        }
    }
}

@Composable
fun CardList(navController: NavController) {
    val menuList = listOf(
        "내 정보",
        "이용 약관",
        "보유 행성"
    )
    Column {
        menuList.forEach {
            MenuCard()
        }
    }
}

@Composable
fun MenuCard() {
    Column(
        modifier = Modifier
            .border(width = 0.5.dp, color = Color.Black)
    ) {
       Text("이용 약관")
    }
}

