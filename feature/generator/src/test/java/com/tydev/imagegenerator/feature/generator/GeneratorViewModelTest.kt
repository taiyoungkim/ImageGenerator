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

import com.tydev.core.imagegenerator.domain.GenerateImageUseCase
import com.tydev.core.imagegenerator.domain.GetPromptUseCase
import com.tydev.core.imagegenerator.testing.repository.TestGeneratorRepository
import com.tydev.core.imagegenerator.testing.util.MainDispatcherRule
import com.tydev.imagegenerator.core.model.data.Chat
import com.tydev.imagegenerator.core.model.data.Choice
import com.tydev.imagegenerator.core.model.data.DataItem
import com.tydev.imagegenerator.core.model.data.Image
import com.tydev.imagegenerator.core.model.data.Message
import com.tydev.imagegenerator.core.model.data.Role
import com.tydev.imagegenerator.core.model.data.Usage
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GeneratorViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val generatorRepository = TestGeneratorRepository()
    private val getPromptUseCase = GetPromptUseCase(generatorRepository)
    private val generateImageUseCase = GenerateImageUseCase(generatorRepository)

    private lateinit var viewModel: GeneratorViewModel

    @Before
    fun setup() {
        viewModel = GeneratorViewModel(
            getPromptUseCase = getPromptUseCase,
            generateImageUseCase = generateImageUseCase,
        )
    }

    @Test
    fun state_Is_Initially_Initial() = runTest {
        assertEquals(
            GeneratorUiState.Initial,
            viewModel.generatorState.value,
        )
    }

    @Test
    fun state_Is_Loading_When_Generate_prompt_Are_Loading() = runTest {
        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.generatorState.collect() }

        viewModel.searchForPrompt("cat")

        assertEquals(
            GeneratorUiState.Loading,
            viewModel.generatorState.value,
        )

        collectJob.cancel()
    }

    @Test
    fun state_Is_Success_When_UseCases_Succeed() = runTest {
        val expectedChat = Chat(
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
        val expectedImage = Image(
            created = 1589478378,
            data = listOf(
                DataItem(
                    url = "url1",
                ),
                DataItem(
                    url = "url2",
                ),
                DataItem(
                    url = "url3",
                ),
                DataItem(
                    url = "url4",
                ),
            ),
        )

        viewModel.searchForPrompt("cat")

        generatorRepository.sendWord(expectedChat)
        generatorRepository.sendPrompt(expectedImage)

        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.generatorState.collect() }

        assertTrue(viewModel.generatorState.value is GeneratorUiState.Success)
        val successState = viewModel.generatorState.value as GeneratorUiState.Success
        assertEquals(expectedChat, successState.chat)
        assertEquals(expectedImage, successState.image)

        collectJob.cancel()
    }

    @Test
    fun state_Is_Error_When_GetPrompt_ThrowsException() = runTest {
        val exception = IOException("Get Prompt failed")
        generatorRepository.throwOnGetPrompt(exception)

        viewModel.searchForPrompt("cat")

        val errorState =
            viewModel.generatorState.first { it is GeneratorUiState.Error } as GeneratorUiState.Error

        assertEquals(exception, errorState.exception)
    }

    @Test
    fun state_Is_Error_When_GenerateImages_ThrowsException() = runTest {
        val exception = IOException("Generate Images failed")
        generatorRepository.throwOnGenerateImages(exception)

        viewModel.generateImage(
            Chat(
                "",
                "",
                0,
                listOf(Choice(0, Message("", "error"), "")),
                Usage(0, 0, 0),
            ),
        )

        val errorState =
            viewModel.generatorState.first { it is GeneratorUiState.Error } as GeneratorUiState.Error

        assertEquals(exception, errorState.exception)
    }
}
