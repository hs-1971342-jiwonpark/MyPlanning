package com.example.planet.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.data.model.CommentBody
import com.example.data.model.CommentUser
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
    val commentList by viewModel.commentList.collectAsState()
    val selectedSorting by viewModel.selectedSorting.collectAsState()
    var imageUri by remember { mutableStateOf<Uri?>(Uri.EMPTY) }
    var text by remember { mutableStateOf("") }
    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }
    val user = viewModel.takeUser

    val onFavoriteClick: (CommentUser) -> Unit = { cUser ->
        var count = cUser.likeCount.toInt()
        val isFavorite = !cUser.isLiked
        if (isFavorite) {
            viewModel.addLike(user.value.uid, cUser.cid, cUser.coId.toString())
            count++
        } else {
            viewModel.removeLike(user.value.uid, cUser.cid, cUser.coId.toString())
            count--
        }
        viewModel.updateCommentLikeStatus(count, isFavorite, cUser.coId)
    }

    PlanetPostScreen(
        uiState = planetPostUiState,
        selectedSorting = selectedSorting,
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
        onFavoriteClick = onFavoriteClick,
        text,
        imageUri,
        commentList,
        { viewModel.sortedByRecent() },
        { viewModel.sortedByLiked() }
    )
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun PlanetPostScreen(
    uiState: PlanetPostUiState,
    selectedSorting : String,
    onClick: () -> Unit,
    onImageClick: () -> Unit,
    onValueChange: (String) -> Unit,
    onFavoriteClick: (CommentUser) -> Unit,
    text: String,
    imageUri: Uri?,
    commentList: List<CommentUser>,
    onRecentClicked: () -> Unit,
    onLikedClicked: () -> Unit
) {

    when (uiState) {
        PlanetPostUiState.Error -> ErrorPage("에러")
        PlanetPostUiState.Loading -> LoadingScreen(text = "로딩")
        is PlanetPostUiState.Success -> Scaffold(
            containerColor = main,
            contentColor = Color.White,
            topBar = {
                Text(
                    text = uiState.card.keyWord,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    textAlign = TextAlign.Center
                )
            },
            bottomBar = {
                Row(
                    horizontalArrangement = Arrangement.Start, // 왼쪽 정렬
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
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
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
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
                            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                            .height(400.dp),
                        model = uiState.card.image,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(
                                    bottomStart = 20.dp,
                                    bottomEnd = 20.dp
                                )
                            )
                            .background(SubMain)
                            .fillMaxWidth()
                            .padding(10.dp),
                        text = uiState.card.description,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(Modifier.height(20.dp))
                }

                item {
                    Toggles(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp),
                        text1 = "최신 순",
                        text2 = "좋아요 순",
                        clickable1 = { onRecentClicked() },
                        clickable2 = { onLikedClicked() },
                        selectedOption = selectedSorting
                    )
                }

                items(commentList.size) { index ->
                    ContentCard(commentList[index], onFavoriteClick)
                }
            }
        }
    }

}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ContentCard(commentUser: CommentUser, onFavoriteClick: (CommentUser) -> Unit) {

    val interactionSource = remember { MutableInteractionSource() }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
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
                modifier = Modifier.fillMaxWidth()
            ) {
                GlideImage(
                    modifier = Modifier.clip(CircleShape),
                    model = commentUser.profile,
                    contentDescription = null
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = commentUser.name,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (commentUser.isLiked) {
                        Icons.Default.Favorite
                    } else {
                        Icons.Default.FavoriteBorder
                    },
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            onFavoriteClick(commentUser)
                        }
                )
                Spacer(Modifier.width(5.dp))
                Text(
                    text = commentUser.likeCount.toString(),
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(Modifier.width(10.dp))
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
            Spacer(Modifier.height(10.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = commentUser.body.comment.toString(),
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(Modifier.height(16.dp))
        }
    }
}