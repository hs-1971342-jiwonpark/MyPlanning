package com.example.login

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import com.example.navigation.Dest
import com.example.navigation.FeatureGraph
import com.example.navigation.NavigationDest
import dagger.hilt.android.lifecycle.HiltViewModel

val uri = "myapp://main"

interface LoginFeature : FeatureGraph

class LoginFeatureImpl : LoginFeature {
    override fun navGraph(
        navHostController: NavHostController,
        navGraphBuilder: NavGraphBuilder,
        provide : Any?
    ) {
        navGraphBuilder.navigation<NavigationDest.LoginRoute>(startDestination = Dest.LoginRoute) {
            composable<Dest.LoginRoute>(
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern = "$uri/login"
                    }
                )
            ) {
                LoginScreen(hiltViewModel(), navHostController)
            }
        }
    }

}

