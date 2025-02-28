package com.example.mypage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.example.mypage.ui.MyPageScreen
import com.example.navigation.Dest
import com.example.navigation.FeatureGraph
import com.example.navigation.NavigationDest
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation

const val uri = "myapp://main"

interface MyPageFeature : FeatureGraph

class MyPageFeatureImpl : MyPageFeature {
    override fun navGraph(
        navHostController: NavHostController,
        navGraphBuilder: NavGraphBuilder,
        provide : Any?
    ) {
        navGraphBuilder.navigation<NavigationDest.MyPageRoute>(startDestination = Dest.MyPageRoute) {
            composable<Dest.MyPageRoute>(
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern = "$uri/myPage"
                    }
                )
            ) {
                MyPageScreen(navHostController)
            }
        }
    }

}

