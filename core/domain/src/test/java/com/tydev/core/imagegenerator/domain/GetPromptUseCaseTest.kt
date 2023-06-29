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

import com.tydev.core.imagegenerator.testing.repository.TestGeneratorRepository
import com.tydev.core.imagegenerator.testing.util.MainDispatcherRule
import com.tydev.imagegenerator.core.model.data.Chat
import com.tydev.imagegenerator.core.model.data.Choice
import com.tydev.imagegenerator.core.model.data.Message
import com.tydev.imagegenerator.core.model.data.Role
import com.tydev.imagegenerator.core.model.data.Usage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue

class GetPromptUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val generatorRepository = TestGeneratorRepository()

    val useCase = GetPromptUseCase(generatorRepository)

    @Test
    fun getPromptUseCase_OnEmptyInput_throwsException() = runTest {
        val word = ""

        assertFails {
            useCase(word).first()
        }.apply {
            assertEquals(GetPromptUseCase.WORD_EMPTY, message)
        }
    }

    @Test
    fun getPromptUseCase_OnLongInput_throwsException() = runTest {
        val word = "a".repeat(21)

        assertFails {
            useCase(word).first()
        }.apply {
            assertEquals(GetPromptUseCase.WORD_OVER_LIMIT, message)
        }
    }

    @Test
    fun getPromptUseCase_OnInput_getPrompt() = runTest {
        val word = "cat"
        val responsePrompt = useCase(word)

        generatorRepository.sendWord(testChat)

        assertTrue(responsePrompt.first().choices.first().message.content.isNotEmpty())
    }
}

private val testChat = Chat(
    id = "chatcmpl-123",
    objectValue = "chat.completion",
    created = 1,
    choices = listOf(
        Choice(
            index = 0,
            message = Message(
                role = Role.USER.name.lowercase(),
                content = "anything of cat",
            ),
            finishReason = "stop",
        ),
    ),
    usage = Usage(
        promptTokens = 9,
        completionTokens = 12,
        totalTokens = 21,
    ),
)
