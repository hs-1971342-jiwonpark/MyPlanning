package com.example.planet.navigation

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import com.example.data.model.PostType
import com.example.navigation.Dest
import com.example.navigation.FeatureGraph
import com.example.navigation.NavigationDest
import com.example.planet.ui.PreviewScreen
import kotlinx.serialization.Serializable

interface PreviewFeature : FeatureGraph

class PreviewImpl : PreviewFeature {
    override fun navGraph(
        navHostController: NavHostController,
        navGraphBuilder: NavGraphBuilder,
        provide : Any?
    ) {
        navGraphBuilder.navigation<NavigationDest.PreviewRoute>(startDestination = Dest.PreviewRoute(
            initialCardId = "",
            initialPostType = PostType.NOT
        )) {
            composable<Dest.PreviewRoute>(
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern = "$uri/preview"
                    }
                )
            ) {
                PreviewScreen(
                    navController = navHostController
                )
            }
        }
    }
}