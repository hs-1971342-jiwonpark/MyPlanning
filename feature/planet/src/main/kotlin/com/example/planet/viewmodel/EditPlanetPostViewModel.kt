package com.example.planet.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.data.model.UserCard
import com.example.data.repository.UserPrefRepository
import com.example.data.repository.UserRepository
import com.example.planet.navigation.PlanetPostRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditPlanetPostViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val userPrefRepository: UserPrefRepository
) : ViewModel() {
    val cId = savedStateHandle.toRoute<PlanetPostRoute>().cid

    val editPlanetPostUiState: StateFlow<PlanetPostUiState> = editPlanetPostUiState(
        topicId = cId
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PlanetPostUiState.Loading,
    )

    private fun editPlanetPostUiState(
        topicId: String
    ): StateFlow<PlanetPostUiState> {
        return MutableStateFlow<PlanetPostUiState>(PlanetPostUiState.Loading).apply {
            if (topicId.isNotEmpty()) {
                viewModelScope.launch {
                    userRepository.getMainCard(topicId).collect { fetchedCard ->
                        value = if (fetchedCard != null) {
                            PlanetPostUiState.Success(
                                card = fetchedCard
                            )
                        } else {
                            Log.e(
                                "PlanetViewModel",
                                "Error: fetchedCard is nullㅇㄴㅁㄹ for topicId $topicId"
                            )
                            PlanetPostUiState.Error
                        }
                    }
                }
            } else {
                Log.e("PlanetViewModel", "Error: fetchedCard is null for topicId $topicId")
                value = PlanetPostUiState.Error
            }
        }
    }
}

sealed interface EditPlanetPostUiState {
    data class Success(
        val card: UserCard
    ) : EditPlanetPostUiState

    data object Error : EditPlanetPostUiState
    data object Loading : EditPlanetPostUiState
}