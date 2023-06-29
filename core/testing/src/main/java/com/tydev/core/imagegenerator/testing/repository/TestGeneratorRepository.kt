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

package com.tydev.core.imagegenerator.testing.repository

import com.tydev.imagegenerator.core.data.repository.GeneratorRepository
import com.tydev.imagegenerator.core.model.data.Chat
import com.tydev.imagegenerator.core.model.data.Image
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class TestGeneratorRepository : GeneratorRepository {
    /**
     * The backing hot flow for the prompt for testing.
     */
    private val promptFlow: MutableSharedFlow<Chat> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    /**
     * The backing hot flow for the image for testing.
     */
    private val imagesFlow: MutableSharedFlow<Image> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun getPrompt(word: String): Flow<Chat> = promptFlow

    override fun generateImages(prompt: String): Flow<Image> = imagesFlow

    fun sendWord(chat: Chat) {
        promptFlow.tryEmit(chat)
    }

    fun sendPrompt(images: Image) {
        imagesFlow.tryEmit(images)
    }
}
