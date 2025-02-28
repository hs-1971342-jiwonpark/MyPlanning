package com.example.navigation

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel

interface FeatureGraph {
    fun navGraph(navHostController : NavHostController, navGraphBuilder: NavGraphBuilder, provide : Any?)
}