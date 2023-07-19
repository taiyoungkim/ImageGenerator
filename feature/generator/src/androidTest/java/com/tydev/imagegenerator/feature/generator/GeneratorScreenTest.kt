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

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ApplicationProvider
import com.tydev.imagegenerator.core.model.data.Chat
import com.tydev.imagegenerator.core.model.data.Choice
import com.tydev.imagegenerator.core.model.data.DataItem
import com.tydev.imagegenerator.core.model.data.Image
import com.tydev.imagegenerator.core.model.data.Message
import com.tydev.imagegenerator.core.model.data.Role
import com.tydev.imagegenerator.core.model.data.Usage
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class GeneratorScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testInitialGeneratorScreen() = runTest {
        composeTestRule.setContent {
            GeneratorScreen(
                generatorState = GeneratorUiState.Initial,
                onGeneratePrompt = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("CircularProgressIndicator").assertDoesNotExist()
    }

    @Test
    fun testLoadingGeneratorScreen() = runTest {
        composeTestRule.setContent {
            GeneratorScreen(
                generatorState = GeneratorUiState.Loading,
                onGeneratePrompt = {}
            )
        }

        composeTestRule.onNodeWithTag("myProgressBar").assertExists()
    }

    @Test
    fun testErrorGeneratorScreen() {
        val errorCode = "401"
        val error = IllegalArgumentException(errorCode)
        composeTestRule.setContent {
            GeneratorScreen(
                generatorState = GeneratorUiState.Error(error),
                onGeneratePrompt = {}
            )
        }
        val context = ApplicationProvider.getApplicationContext<Context>()
        val errorText = context.getString(GeneratorError.fromCode(errorCode).messageResId)

        composeTestRule.onNodeWithText(errorText).assertExists()
    }

    @Test
    fun testSuccessGeneratorScreen() = runTest {
        val chat = Chat(
            id = "chatcmpl-123",
            objectValue = "chat.completion",
            created = 1,
            choices = listOf(
                Choice(
                    index = 0,
                    message = Message(
                        role = Role.ASSISTANT.name.lowercase(),
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
        val image = Image(
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
            )
        )
        composeTestRule.setContent {
            GeneratorScreen(
                generatorState = GeneratorUiState.Success(chat, image),
                onGeneratePrompt = {}
            )
        }

        // Check the success state UI elements
        composeTestRule.onNodeWithText(chat.choices[0].message.content).assertExists()
        // You can add more assertions here to check the images as well
    }
}