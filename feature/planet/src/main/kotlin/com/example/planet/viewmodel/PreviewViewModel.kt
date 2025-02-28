package com.example.planet.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.data.model.PostType
import com.example.data.model.User
import com.example.data.model.UserCard
import com.example.data.repository.UserPrefRepository
import com.example.data.repository.UserRepository
import com.example.navigation.Dest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreviewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val userPrefRepository: UserPrefRepository
) : ViewModel() {

    private val _peopleCount = MutableStateFlow("")

    val cId = savedStateHandle.toRoute<Dest.PreviewRoute>().initialCardId
    val pType = savedStateHandle.toRoute<Dest.PreviewRoute>().initialPostType

    private val _isParticipatedIn = MutableStateFlow(false)
    val isParticipatedIn : StateFlow<Boolean> = _isParticipatedIn

    val previewUiState: StateFlow<PreviewUiState> = previewUiState(
        topicId = cId,
        postType = pType
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PreviewUiState.Loading,
    )

    init {
        confirmIsParticipated(cId)
    }

    private fun confirmIsParticipated(cardId: String){
        viewModelScope.launch {
           val uid = userPrefRepository.getUserPrefs().firstOrNull()?.uid
            userRepository.confirmParticipateInUser(cardId = cardId, uid = uid.toString()).collect{ isParticipatedIn ->
                _isParticipatedIn.value = isParticipatedIn
            }
        }
    }

    private fun previewUiState(
        topicId: String,
        postType: PostType
    ): StateFlow<PreviewUiState> {
        return MutableStateFlow<PreviewUiState>(PreviewUiState.Loading).apply {
            Log.d("상태",topicId + postType)
            if (topicId.isNotEmpty()) {
                viewModelScope.launch {
                    userRepository.getMainCard(topicId).collect { fetchedCard ->
                        value = if (fetchedCard != null) {
                            _peopleCount.value = fetchedCard.participatePeople
                            PreviewUiState.Success(
                                cid = topicId,
                                postType = postType,
                                userCard = fetchedCard
                            )
                        } else {
                            Log.e("PlanetViewModel", "Error: fetchedCard is nullㅇㄴㅁㄹ for topicId $topicId")
                            PreviewUiState.Error
                        }
                    }
                }
            } else {
                Log.e("PlanetViewModel", "Error: fetchedCard is null for topicId $topicId")
                value = PreviewUiState.Error
            }
        }
    }

    fun upDateParticipateInPeople(cardId: String){
        viewModelScope.launch {
            val user = userPrefRepository.getUserPrefs().firstOrNull() ?: User()
            Log.d("유저","$user")
            userRepository.updateParticipateInUser(
                cardId,
                (_peopleCount.value.toInt()+1).toString(),
                user
            )
        }
    }

}

sealed interface PreviewUiState {
    data class Success(
        val cid: String,
        val postType: PostType,
        val userCard: UserCard
    ) : PreviewUiState
    
    data object Error : PreviewUiState
    data object Loading : PreviewUiState
}