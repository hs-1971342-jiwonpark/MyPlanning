package com.example.planet.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.data.model.CommentBody
import com.example.data.model.CommentUser
import com.example.data.model.UserCard
import com.example.designsystem.component.text.ErrorPage
import com.example.designsystem.component.text.ImageTextField
import com.example.designsystem.component.text.LoadingScreen
import com.example.designsystem.component.text.Toggles
import com.example.designsystem.theme.SubMain
import com.example.designsystem.theme.main
import com.example.planet.viewmodel.PlanetPostUiState
import com.example.planet.viewmodel.PlanetPostViewModel

@Composable
internal fun PlanetPostScreen(
    viewModel: PlanetPostViewModel = hiltViewModel(),
    navController: NavController
) {
    val planetPostUiState by viewModel.planetPostUiState.collectAsState()
    var imageUri by remember { mutableStateOf<Uri?>(Uri.EMPTY) }
    var text by remember { mutableStateOf("") }
    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? -> imageUri = uri }

    PlanetPostScreen(
        uiState = planetPostUiState,
        onClick = {
            if (text.isNotBlank()) {
                val cUser = CommentUser()
                val comment = CommentBody()
                comment.comment = text
                cUser.body = comment
                viewModel.addComment(cUser, imageUri)
                text = ""  // 전송 후 텍스트 필드 초기화
                imageUri = Uri.EMPTY
            }
        },
        onImageClick = { imagePickerLauncher.launch("image/*") },
        onValueChange = { text = it },
        text,
        imageUri
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
internal fun PlanetPostScreen(
    uiState: PlanetPostUiState,
    onClick: () -> Unit,
    onImageClick: () -> Unit,
    onValueChange: (String) -> Unit,
    text: String,
    imageUri: Uri?
) {

    when (uiState) {
        PlanetPostUiState.Error -> ErrorPage("에러")
        PlanetPostUiState.Loading -> LoadingScreen(text = "로딩")
        is PlanetPostUiState.Success -> Scaffold(
            containerColor = main,
            contentColor = Color.White,
            modifier = Modifier.padding(bottom = 16.dp),
            bottomBar = {
                Row(
                    horizontalArrangement = Arrangement.Start, // 왼쪽 정렬
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    ImageTextField(
                        img = imageUri,
                        text = text,
                        onMessageSend = onClick,
                        onValueChange = { onValueChange(it) },
                        onImageClick = { onImageClick() },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        ) { paddingValue ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValue)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Row(
                        Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = uiState.card.keyWord + "행성에 오신 것을 환영합니다!!",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                }
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        GlideImage(
                            modifier = Modifier.clip(CircleShape),
                            model = uiState.card.ownerProfile,
                            contentDescription = null
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = uiState.card.ownerName,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
                item {
                    GlideImage(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .height(400.dp),
                        model = uiState.card.image,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
                item {
                    Text(
                        modifier = Modifier
                            .background(SubMain)
                            .fillParentMaxWidth()
                            .padding(5.dp),
                        text = uiState.card.description,
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                item {
                    Toggles(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp),
                        text1 = "최신 순",
                        text2 = "참여자 순",
                        clickable1 = {},
                        clickable2 = {}
                    )
                }

                items(uiState.commentList.size) { index ->
                    ContentCard(uiState.commentList[index])
                }
            }
        }
    }

}

@Preview
@Composable
fun d() {
    PlanetPostScreen(
        uiState = PlanetPostUiState.Success(
            card = UserCard(),
            commentList = listOf()
        ),
        onClick = { },
        onImageClick = { },
        onValueChange = { },
        "",
        Uri.EMPTY
    )
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ContentCard(commentUser: CommentUser) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
        ,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
        colors = CardColors(
            contentColor = Color.White,
            disabledContentColor = main,
            containerColor = SubMain,
            disabledContainerColor = main
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                GlideImage(
                    modifier = Modifier.clip(CircleShape),
                    model = commentUser.profile,
                    contentDescription = null
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = commentUser.name,
                    style = MaterialTheme.typography.labelLarge
                )

            }
            Spacer(Modifier.height(10.dp))
            if (commentUser.body.image != null) {
                GlideImage(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .aspectRatio(1f),
                    contentScale = ContentScale.Crop,
                    model = commentUser.body.image,
                    contentDescription = null
                )
            }
            Spacer(Modifier.height(20.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = commentUser.body.comment.toString(),
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(Modifier.height(30.dp))
        }
    }
}