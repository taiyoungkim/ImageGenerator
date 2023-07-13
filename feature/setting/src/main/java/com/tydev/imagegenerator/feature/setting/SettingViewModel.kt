package com.tydev.imagegenerator.feature.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tydev.core.imagegenerator.domain.FetchUserDataAndUpdateKeyUseCase
import com.tydev.core.imagegenerator.domain.GetUserDataUseCase
import com.tydev.imagegenerator.core.model.data.UserData
import com.tydev.imagegenerator.feature.setting.SettingUiState.Error
import com.tydev.imagegenerator.feature.setting.SettingUiState.Loading
import com.tydev.imagegenerator.feature.setting.SettingUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val fetchUserDataAndUpdateKeyUseCase: FetchUserDataAndUpdateKeyUseCase,
) : ViewModel() {
    init {
        viewModelScope.launch { getUserDataUseCase().first() }
    }

    val uiState: StateFlow<SettingUiState> = getUserDataUseCase()
        .map {
            if (it.apiKey.isEmpty()) {
                Error(KotlinNullPointerException())
            } else {
                Success(it)
            }
        }.stateIn(
            scope = viewModelScope,
            initialValue = Loading,
            started = SharingStarted.WhileSubscribed(5_000),
        )

    fun setApiKey(apiKey: String) {
        viewModelScope.launch {
            fetchUserDataAndUpdateKeyUseCase(apiKey)
            getUserDataUseCase().first()
        }
    }
}

sealed interface SettingUiState {
    object Loading : SettingUiState
    data class Success(val userData: UserData) : SettingUiState
    data class Error(val exception: Throwable) : SettingUiState
}
