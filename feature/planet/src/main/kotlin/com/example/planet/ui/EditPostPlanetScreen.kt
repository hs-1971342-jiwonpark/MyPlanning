package com.example.planet.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.planet.viewmodel.EditPlanetPostUiState
import com.example.planet.viewmodel.EditPlanetPostViewModel
import com.example.planet.viewmodel.PlanetPostUiState

@Composable
internal fun EdiPostPlanetScreen(
    viewModel: EditPlanetPostViewModel = hiltViewModel(),
    navController: NavController
) {
    val editPlanetPostUiState by viewModel.editPlanetPostUiState.collectAsState()
    EdiPostPlanetScreen(
        uiState = editPlanetPostUiState,
        onClick = {}
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
internal fun EdiPostPlanetScreen(
    uiState: EditPlanetPostUiState,
    onClick: () -> Unit
) {

}

