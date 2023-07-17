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

package com.tydev.imagegenerator.core.network.fake

import com.tydev.imagegenerator.core.model.data.Chat
import com.tydev.imagegenerator.core.model.data.Choice
import com.tydev.imagegenerator.core.model.data.DataItem
import com.tydev.imagegenerator.core.model.data.Image
import com.tydev.imagegenerator.core.model.data.Message
import com.tydev.imagegenerator.core.model.data.Role
import com.tydev.imagegenerator.core.model.data.Usage
import com.tydev.imagegenerator.core.model.request.CompletionBody
import com.tydev.imagegenerator.core.model.request.GenerateImageBody
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class FakeNetworkDataSourceTest {

    private lateinit var subject: FakeNetworkDataSource

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        subject = FakeNetworkDataSource(
            ioDispatcher = testDispatcher,
        )
    }

    @Test
    fun getPrompt_OnValidInput_returnsExpectedChat() = runTest(testDispatcher) {
        assertEquals(
            Chat(
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
            ),
            subject.getPrompt(CompletionBody("", listOf())),
        )
    }

    @Test
    fun generateImage_OnValidInput_returnsExpectedImage() = runTest(testDispatcher) {
        assertEquals(
            Image(
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
            ),
            subject.generateImage(GenerateImageBody("", 0, "")),
        )
    }
}
