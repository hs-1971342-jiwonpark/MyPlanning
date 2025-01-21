package com.example.planet.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.data.model.UserCard
import com.example.data.repository.UserPrefRepository
import com.example.data.repository.UserRepository
import com.example.planet.navigation.PreviewRoute
import com.example.planet.ui.PostType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlanetViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val userPrefRepository: UserPrefRepository
) : ViewModel() {

    private val _cardData = MutableStateFlow<List<UserCard>>(emptyList())
    val cardData: StateFlow<List<UserCard>> = _cardData

    private val _postType = MutableStateFlow(PostType.NOT)
    val postType: StateFlow<PostType> = _postType

    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady

    private val _planetUiState = MutableStateFlow<PlanetUiState>(PlanetUiState.Loading)
    val planetUiState: StateFlow<PlanetUiState> = _planetUiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PlanetUiState.Loading,
    )
    fun refresh() {
        _planetUiState.value = PlanetUiState.Loading
        fetchCardListData()
    }
    init {
        try {
            val route = savedStateHandle.toRoute<PreviewRoute>()
            Log.d("ViewModel", "Route: $route")
            Log.d("ViewModel", "cId: ${route.initialCardId}, pType: ${route.initialPostType}")
            fetchCardListData()
        } catch (e: Exception) {
            Log.e("ViewModel", "Error getting route", e)
        }
    }

    private fun fetchCardListData() {
        viewModelScope.launch {
            try {
                userRepository.getMainCardList().collect { cards ->
                    _cardData.value = cards
                    _planetUiState.value = PlanetUiState.Success(
                        cardList = cards,
                        type = _postType.value
                    )
                }
            } catch (e: Exception) {
                _planetUiState.value = PlanetUiState.Error
            }
        }
    }

    fun confirmPostType(cardOwnerId: String) {
        Log.d("confirmPostType", "Method called with cardOwnerId: $cardOwnerId")
        try {
            viewModelScope.launch {
                userPrefRepository.getUserPrefs().collect { user ->
                    Log.d("confirmPostType", "Comparing $cardOwnerId with ${user.uid}")
                    if (cardOwnerId == user.uid) {
                        _postType.value = PostType.ME
                        Log.d("confirmPostType", "PostType set to ME")
                    } else {
                        _postType.value = PostType.OTHER
                        Log.d("confirmPostType", "PostType set to OTHER")
                    }

                    setIsReady(true)
                    // Update UI state with new post type
                    updateUiStateWithCurrentData()
                }
            }
        } catch (e: Exception) {
            Log.e("confirmPostType", "Error: ${e.message}")
        }
    }

    private fun updateUiStateWithCurrentData() {
        if (_planetUiState.value is PlanetUiState.Success) {
            _planetUiState.value = PlanetUiState.Success(
                cardList = _cardData.value,
                type = _postType.value
            )
        }
    }

    fun setIsReady(isReady: Boolean) {
        viewModelScope.launch {
            _isReady.value = isReady
        }
    }

}

sealed interface PlanetUiState {
    data class Success(
        val cardList: List<UserCard>,
        val type : PostType
    ) : PlanetUiState

    data object Error : PlanetUiState
    data object Loading : PlanetUiState
}