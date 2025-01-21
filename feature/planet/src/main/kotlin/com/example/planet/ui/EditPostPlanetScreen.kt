package com.example.planet.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.designsystem.component.text.ErrorPage
import com.example.designsystem.component.text.LoadingScreen
import com.example.designsystem.theme.SubMain
import com.example.designsystem.theme.main
import com.example.planet.navigation.EditPlanetPostRoute
import com.example.planet.viewmodel.EditPlanetPostViewModel
import com.example.planet.viewmodel.PlanetPostUiState
import com.example.planet.viewmodel.PlanetPostViewModel

@Composable
internal fun EdiPostPlanetScreen(
    viewModel: EditPlanetPostViewModel = hiltViewModel(),
    navController: NavController
) {
    val editPlanetPostUiState by viewModel.editPlanetPostUiState.collectAsState()
    PlanetPostScreen(
        uiState = editPlanetPostUiState,
        onClick = {}
        )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
internal fun EdiPostPlanetScreen(
    uiState: PlanetPostUiState,
    onClick: () -> Unit
) {

}

