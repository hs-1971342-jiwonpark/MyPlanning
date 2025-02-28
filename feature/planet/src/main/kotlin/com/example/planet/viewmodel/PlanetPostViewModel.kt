package com.example.planet.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.data.model.CommentUser
import com.example.data.model.UserCard
import com.example.data.repository.UserPrefRepository
import com.example.data.repository.UserRepository
import com.example.navigation.Dest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
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

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _planetPostUiState.value = PlanetPostUiState.Loading
            try {
                val fetchedCard = userRepository.getMainCard(cId).first()
                val commentList = userRepository.getCommentList(cId).first()

                _planetPostUiState.value = if (fetchedCard != null) {
                    PlanetPostUiState.Success(
                        card = fetchedCard,
                        commentList = commentList
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
                Log.d("추가","${newComment.body}")
                userRepository.addComment(cId, newComment)

                refresh() // 댓글 추가 후 상태 갱신
            } catch (e: Exception) {
                Log.e("PlanetPostViewModel", "Failed to add comment", e)
                _planetPostUiState.value = PlanetPostUiState.Error
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