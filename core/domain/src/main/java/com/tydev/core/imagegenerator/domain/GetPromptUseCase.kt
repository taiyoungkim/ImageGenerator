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

package com.tydev.core.imagegenerator.domain

import com.tydev.imagegenerator.core.data.repository.GeneratorRepository
import com.tydev.imagegenerator.core.model.data.Chat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPromptUseCase @Inject constructor(
    private val generatorRepository: GeneratorRepository,
) {
    operator fun invoke(word: String): Flow<Chat> = flow {
        if (word.isEmpty()) {
            throw IllegalArgumentException(WORD_EMPTY)
        } else if (word.length > WORD_LIMIT) {
            throw IllegalArgumentException(WORD_OVER_LIMIT)
        }

        emitAll(generatorRepository.getPrompt(word))
    }

    companion object {
        const val WORD_EMPTY = "1"
        const val WORD_OVER_LIMIT = "2"
        const val WORD_LIMIT = 50
    }
}
