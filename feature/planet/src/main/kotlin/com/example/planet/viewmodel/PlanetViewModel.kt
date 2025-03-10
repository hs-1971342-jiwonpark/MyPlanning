package com.example.planet.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
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
class PlanetViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val userPrefRepository: UserPrefRepository
) : ViewModel() {

    private val _cardData = MutableStateFlow<List<UserCard>>(emptyList())
    val cardData: StateFlow<List<UserCard>> = _cardData

    private val _pageData = MutableStateFlow<List<UserCard>>(emptyList())
    val pageData: StateFlow<List<UserCard>> = _pageData

    private val _postType = MutableStateFlow(PostType.NOT)
    val postType: StateFlow<PostType> = _postType

    private val _selectedSorting = MutableStateFlow("최신 순")  // 초기값은 최신순
    val selectedSorting: StateFlow<String> = _selectedSorting

    private val _isFirst = MutableStateFlow(true)
    val isFirst: StateFlow<Boolean> = _isFirst

    private val _planetUiState = MutableStateFlow<PlanetUiState>(PlanetUiState.Loading)

    val planetUiState: StateFlow<PlanetUiState> = _planetUiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PlanetUiState.Loading,
    )

    init {
        viewModelScope.launch {
            val userPrefs = userPrefRepository.getUserPrefs().first()
            Log.d("초기화","${userPrefRepository.getUserPrefs().first()}")
            _isFirst.value = userPrefs.isFirst
            fetchCardListData()
        }
    }

    fun setIsFirst() {
        viewModelScope.launch {
            _isFirst.emit(false)
            userRepository.updateUser(userPrefRepository.getUserPrefs().first().uid)
        }
    }

    fun filteredCard(text: String) {
        if (text.isEmpty()) {
            _planetUiState.value = PlanetUiState.Success(
                pageData = _pageData.value,
                cardList = _cardData.value,  // 원본 리스트 유지
                type = _postType.value,
                isFirst = _isFirst.value
            )
        } else {
            val filterList = _cardData.value.filter {
                it.keyWord.contains(text, ignoreCase = true) ||  // 키워드에 검색어 포함
                        it.ownerName.contains(text, ignoreCase = true)   // 소유자 이름에 검색어 포함
            }
            _planetUiState.value = PlanetUiState.Success(
                pageData = _pageData.value,
                cardList = filterList,
                type = _postType.value,
                isFirst=_isFirst.value
            )
        }
    }


    fun sortedByRecent() {
        viewModelScope.launch {
            val sortedList = _cardData.value.sortedBy { it.cid }
            _cardData.value = sortedList
            _planetUiState.value = PlanetUiState.Success(
                pageData = _pageData.value,
                cardList = sortedList,
                type = _postType.value,
                isFirst = userPrefRepository.getUserPrefs().first().isFirst
            )
            _selectedSorting.value = "최신 순"
        }
    }

    fun sortedByPopular() {
        val sortedList = _cardData.value.sortedByDescending { it.participatePeople }
        _cardData.value = sortedList
        _planetUiState.value = PlanetUiState.Success(
            pageData = _pageData.value,
            cardList = sortedList,
            type = _postType.value,
            isFirst = _isFirst.value
        )
        _selectedSorting.value = "참여자 순"
    }

    fun fetchCardListData() {
        viewModelScope.launch {
            sortedByRecent()
            try {
                userRepository.getMainCardList().collect { cards ->
                    _cardData.value = cards
                    _pageData.value = cards.sortedByDescending { it.participatePeople }
                    _planetUiState.value = PlanetUiState.Success(
                        pageData = _pageData.value,
                        cardList = cards,
                        type = _postType.value,
                        isFirst = _isFirst.value
                    )
                }
            } catch (e: Exception) {
                _planetUiState.value = PlanetUiState.Error
            }
        }
    }

    fun confirmPostType(cardOwnerId: String, onComplete: (PostType) -> Unit) {
        Log.d("confirmPostType", "Method called with cardOwnerId: $cardOwnerId")
        try {
            _planetUiState.value = PlanetUiState.Loading
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
        _planetUiState.value = PlanetUiState.Success(
            pageData = _pageData.value,
            cardList = _cardData.value,
            type = postType,
            isFirst = _isFirst.value
        )
    }

}

sealed interface PlanetUiState {
    data class Success(
        val pageData: List<UserCard>,
        val cardList: List<UserCard>,
        val type: PostType,
        val isFirst : Boolean
    ) : PlanetUiState

    data object Error : PlanetUiState
    data object Loading : PlanetUiState
}