package com.example.mypage.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.PostType
import com.example.data.model.UserCard
import com.example.data.repository.UserPrefRepository
import com.example.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HoldPlanetViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userPrefRepository: UserPrefRepository
) : ViewModel() {
    private val _holdUiState = MutableStateFlow<HoldPlanetUiState>(HoldPlanetUiState.Loading)

    val holdUiState: StateFlow<HoldPlanetUiState> = _holdUiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HoldPlanetUiState.Loading,
    )

    private val _cardData = MutableStateFlow<List<UserCard>>(emptyList())
    val cardData: StateFlow<List<UserCard>> = _cardData

    private val _postType = MutableStateFlow(PostType.NOT)
    val postType: StateFlow<PostType> = _postType

    init {
        fetchCardListData()
    }

    private fun fetchCardListData() {
        viewModelScope.launch {
            try {
                val cardIdList =
                    userRepository.getHoldCardList(userPrefRepository.getUserPrefs().first().uid)
                        .first()
                val cardList = mutableListOf<UserCard>()

                cardIdList.forEach {
                    userRepository.getMainCard(it).first()?.let { card -> cardList.add(card) }
                }
                Log.d("카드","$cardList")
                _cardData.value = cardList
                _holdUiState.value = HoldPlanetUiState.Success(
                    cardList = cardList,
                    type = _postType.value
                )

            } catch (e: Exception) {
                _holdUiState.value = HoldPlanetUiState.Error
            }
        }
    }

    fun confirmPostType(cardOwnerId: String, onComplete: (PostType) -> Unit) {
        Log.d("confirmPostType", "Method called with cardOwnerId: $cardOwnerId")
        try {
            _holdUiState.value = HoldPlanetUiState.Loading
            viewModelScope.launch {
                userPrefRepository.getUserPrefs().collect { user ->
                    Log.d("confirmPostType", "Comparing $cardOwnerId with ${user.uid}")
                    val postType: PostType = if (cardOwnerId == user.uid) {
                        PostType.ME
                    } else {
                        PostType.OTHER
                    }
                    updateUiStateWithCurrentData(postType)
                    onComplete(postType)
                }
            }
        } catch (e: Exception) {
            Log.e("confirmPostType", "Error: ${e.message}")
        }
    }

    private fun updateUiStateWithCurrentData(postType: PostType) {
        Log.d("아이디 뷰모델", "$postType")
        _postType.value = postType
        _holdUiState.value = HoldPlanetUiState.Success(
            cardList = _cardData.value,
            type = postType
        )
    }


}


sealed interface HoldPlanetUiState {
    data class Success(
        val cardList: List<UserCard>,
        val type: PostType
    ) : HoldPlanetUiState

    data object Error : HoldPlanetUiState
    data object Loading : HoldPlanetUiState
}