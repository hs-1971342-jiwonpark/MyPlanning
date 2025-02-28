package com.example.planet.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.UserCard
import com.example.data.repository.UserPrefRepository
import com.example.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MakePlanetViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userPrefRepository: UserPrefRepository
) : ViewModel() {

    private val _imageUri = MutableStateFlow(String())
    val imageUri: StateFlow<String> = _imageUri

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess : StateFlow<Boolean> = _isSuccess

    private val _uploadState = MutableStateFlow(UploadState.IDLE)
    val uploadState : StateFlow<UploadState> = _uploadState

    fun saveImgUri(imageUri: Uri, card: UserCard){
        _uploadState.value = UploadState.LOADING
        try {
            viewModelScope.launch {
                val user = userPrefRepository.getUserPrefs().firstOrNull()
                val fUid = user?.uid
                val fCid = userRepository.getRecentStorageCardId().firstOrNull() ?: 0
                with(card) {
                    userId = fUid
                    cid = fCid + 1
                    Log.d("카드","$cid")
                    ownerName = user?.name ?: ""
                    userRepository.cardUpload(imageUri,card).collect {
                        _imageUri.value = it
                        card.image = it
                        card.ownerProfile = user?.photoUrl.toString()
                        val isSuccess = userRepository.saveCard(card).firstOrNull() ?: false
                        updateSuccessState(isSuccess)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("MakePlanetViewModel", "Error")
        }
    }

    fun updateSuccessState(isSuccess : Boolean){
        viewModelScope.launch {
            _isSuccess.value = isSuccess
            if(isSuccess) {
                _uploadState.value = UploadState.SUCCESS
            }
            else{
                _uploadState.value = UploadState.IDLE
            }
        }
    }
}

enum class UploadState{
    IDLE, LOADING, SUCCESS
}
