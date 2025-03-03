package com.example.planet.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.data.model.CommentUser
import com.example.data.model.User
import com.example.data.model.UserCard
import com.example.data.repository.UserPrefRepository
import com.example.data.repository.UserRepository
import com.example.navigation.Dest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlanetPostViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val userPrefRepository: UserPrefRepository
) : ViewModel() {
    val cId = savedStateHandle.toRoute<Dest.PlanetPostRoute>().cid

    private val _commentList = MutableStateFlow<List<CommentUser>>(emptyList())
    val commentList: StateFlow<List<CommentUser>> = _commentList

    private val _planetPostUiState = MutableStateFlow<PlanetPostUiState>(PlanetPostUiState.Loading)
    val planetPostUiState: StateFlow<PlanetPostUiState> = _planetPostUiState

    private val _takeUser = MutableStateFlow(User())
    val takeUser: StateFlow<User> = _takeUser

    init {
        takeUser()
        refresh()
    }

    private fun takeUser(){
        viewModelScope.launch {
            _takeUser.value = userPrefRepository.getUserPrefs().first()
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            _planetPostUiState.value = PlanetPostUiState.Loading
            try {
                val fetchedCard = userRepository.getMainCard(cId).first()
                _commentList.value = userRepository.getCommentList(cId).first()

                _planetPostUiState.value = if (fetchedCard != null) {
                    PlanetPostUiState.Success(
                        card = fetchedCard,
                        commentList = _commentList.value
                    )
                } else {
                    PlanetPostUiState.Error
                }
            } catch (e: Exception) {
                Log.e("PlanetPostViewModel", "Error refreshing data", e)
                _planetPostUiState.value = PlanetPostUiState.Error
            }
        }
    }

    fun addComment(newComment: CommentUser, image: Uri?) {
        viewModelScope.launch {
            _planetPostUiState.value = PlanetPostUiState.Loading
            try {
                val user = userPrefRepository.getUserPrefs().first()
                newComment.cid = cId
                newComment.profile = user.photoUrl
                newComment.name = user.name

                if (image != null) {
                    userRepository.commentImgUpload(image, cId).collect {
                        newComment.body.image = it
                    }
                }
                Log.d("추가", "${newComment.body}")
                userRepository.addComment(cId, newComment)

                refresh() // 댓글 추가 후 상태 갱신
            } catch (e: Exception) {
                Log.e("PlanetPostViewModel", "Failed to add comment", e)
                _planetPostUiState.value = PlanetPostUiState.Error
            }
        }
    }

    fun updateCommentLikeStatus(
        favoriteCount: Int,
        isFavorite: Boolean,
        coId: Int
    ) {
        viewModelScope.launch {
            try {
                _commentList.value = _commentList.value.toMutableList().also { list ->
                    list[coId] = list[coId].copy(
                        isLiked = isFavorite,
                        likeCount = favoriteCount.toLong()
                    )
                }
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
            }
        }
    }

    fun addLike(
        uid: String,
        cid: String,
        coId: String
    ) {
        viewModelScope.launch {
            try {
                userRepository.addLike(uid, cid, coId)
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
            }
        }
    }

    fun removeLike(
        uid: String,
        cid: String,
        coId: String
    ) {
        viewModelScope.launch {
            try {
                userRepository.removeLike(uid, cid, coId)
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
            }
        }
    }
}


sealed interface PlanetPostUiState {
    data class Success(
        val card: UserCard,
        val commentList: List<CommentUser>
    ) : PlanetPostUiState

    data object Error : PlanetPostUiState
    data object Loading : PlanetPostUiState
}