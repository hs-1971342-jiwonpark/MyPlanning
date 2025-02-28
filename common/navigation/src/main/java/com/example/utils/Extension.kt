package com.example.utils

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.navOptions

object Extension {
    fun setNavOptions(navController: NavController) = navOptions {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = false
        }
        launchSingleTop = false
        restoreState = false
    }
}