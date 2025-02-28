package com.example.planet.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.data.model.UserCard
import com.example.designsystem.component.text.LoadingScreen
import com.example.designsystem.component.text.PlanetDescriptionTextField
import com.example.designsystem.component.text.PlanetTitleTextField
import com.example.designsystem.theme.main
import com.example.planet.R
import com.example.planet.viewmodel.MakePlanetViewModel
import com.example.planet.viewmodel.UploadState

@Composable
internal fun MakePlanetScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: MakePlanetViewModel = hiltViewModel()
) {

    var title by rememberSaveable { mutableStateOf("") }

    var description by rememberSaveable { mutableStateOf("") }

    val userCard by rememberSaveable { mutableStateOf(UserCard()) }

    var imageUri by rememberSaveable { mutableStateOf(Uri.EMPTY) }

    val uploadState by viewModel.uploadState.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
        }
    }

    // 갤러리 열기 버튼 클릭 핸들러
    val openGallery = {
        launcher.launch("image/*")
    }

    LaunchedEffect(uploadState) {
        if(uploadState == UploadState.SUCCESS) {
            navController.popBackStack()
        }
    }

    MakePlanetScreen(
        modifier = modifier,
        text = title,
        description = description,
        onValueChange = { title = it },
        onValueChange2 = { description = it },
        onButtonClick = {
            userCard.keyWord = title
            userCard.description = description
            userCard.participatePeople = "0"
            viewModel.saveImgUri(imageUri, userCard)
        },
        onImageClick = openGallery,
        imageUri = imageUri,
        uploadState = uploadState,
        popBack = {  }
    )

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
internal fun MakePlanetScreen(
    modifier: Modifier,
    text: String,
    description: String,
    onValueChange: (String) -> Unit,
    onValueChange2: (String) -> Unit,
    onButtonClick: () -> Unit,
    onImageClick: () -> Unit,
    imageUri: Any,
    uploadState: UploadState,
    popBack: () -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = main
    ) { paddingValues ->
        when (uploadState) {
            UploadState.IDLE -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(main)
                    .padding(paddingValues)
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val widthModifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                Column {
                    Text(
                        modifier = Modifier,
                        text = "행성 이름",
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )
                    PlanetTitleTextField(
                        text = text,
                        onValueChange = onValueChange
                    )
                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )
                    Column(
                        modifier = widthModifier,
                        verticalArrangement = Arrangement.Top
                    ) {
                        GlideImage(
                            model = imageUri.takeIf { it != Uri.EMPTY } ?: R.drawable.ic_add_image,
                            contentDescription = null,
                            contentScale = if (imageUri != Uri.EMPTY) ContentScale.Crop else ContentScale.None,
                            modifier = widthModifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable(onClick = onImageClick)
                        )
                        Spacer(
                            modifier = Modifier.height(20.dp)
                        )
                        PlanetDescriptionTextField(
                            text = description,
                            onValueChange = onValueChange2,
                            modifier = widthModifier
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onButtonClick
                    ) {
                        Text(
                            text = "나만의 행성 만들기",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            UploadState.LOADING -> LoadingScreen(text = "나만의 행성을 만드는 중")
            UploadState.SUCCESS -> {
                popBack()
            }
        }
    }
}


@Preview
@Composable
fun ThisScreens() {
    MakePlanetScreen(
        modifier = Modifier,
        text = "",
        description = "",
        onValueChange = { },
        onValueChange2 = {},
        onButtonClick = { },
        onImageClick = {},
        imageUri = R.drawable.ic_add_image,
        uploadState = UploadState.IDLE,
        popBack = {}
    )
}