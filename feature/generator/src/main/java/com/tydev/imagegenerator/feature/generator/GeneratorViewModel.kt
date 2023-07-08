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

package com.tydev.imagegenerator.feature.generator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tydev.core.imagegenerator.domain.GenerateImageUseCase
import com.tydev.core.imagegenerator.domain.GetPromptUseCase
import com.tydev.imagegenerator.core.model.data.Chat
import com.tydev.imagegenerator.core.model.data.Image
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeneratorViewModel @Inject constructor(
    val getPromptUseCase: GetPromptUseCase,
    val generateImageUseCase: GenerateImageUseCase,
) : ViewModel() {
    private val _generatorState = MutableSharedFlow<GeneratorUiState>(replay = 1)
    val generatorState: StateFlow<GeneratorUiState> = _generatorState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = GeneratorUiState.Initial,
    )

    fun searchForPrompt(word: String) {
        viewModelScope.launch {
            _generatorState.emit(GeneratorUiState.Loading)
            getPromptUseCase(word)
                .catch { exception -> _generatorState.emit(GeneratorUiState.Error(exception)) }
                .collect { chat ->
                    generateImage(chat)
                }
        }
    }

    suspend fun generateImage(chat: Chat) {
        generateImageUseCase(chat.choices[0].message.content)
            .catch { exception -> _generatorState.emit(GeneratorUiState.Error(exception)) }
            .collect { image ->
                _generatorState.emit(GeneratorUiState.Success(chat, image))
            }
    }
}

sealed interface GeneratorUiState {
    object Initial : GeneratorUiState
    object Loading : GeneratorUiState
    data class Success(val chat: Chat, val image: Image) : GeneratorUiState
    data class Error(val exception: Throwable) : GeneratorUiState
}
