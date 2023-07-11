/*
 * Copyright 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tydev.imagegenerator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tydev.imagegenerator.MainActivityUiState.Error
import com.tydev.imagegenerator.MainActivityUiState.Loading
import com.tydev.imagegenerator.MainActivityUiState.Success
import com.tydev.imagegenerator.core.data.repository.UserDataRepository
import com.tydev.imagegenerator.core.model.data.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    userDataRepository: UserDataRepository,
) : ViewModel() {
    init {
        viewModelScope.launch {
            val user = userDataRepository.userData.first()
            userDataRepository.fetchUserDataAndUpdateKey(user)
        }
    }

    val uiState: StateFlow<MainActivityUiState> = userDataRepository.userData
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
}

sealed interface MainActivityUiState {
    object Loading : MainActivityUiState
    data class Success(val userData: UserData) : MainActivityUiState
    data class Error(val exception: Throwable) : MainActivityUiState
}
