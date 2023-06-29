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
import com.tydev.imagegenerator.core.model.data.DataItem
import com.tydev.imagegenerator.core.model.data.Image
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue

class GenerateImageUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val generatorRepository = TestGeneratorRepository()

    val useCase = GenerateImageUseCase(generatorRepository)

    @Test
    fun generateImageUseCase_OnEmptyInput_throwsException() = runTest {
        val prompt = ""

        assertFails {
            useCase(prompt).first()
        }.apply {
            assertEquals(GenerateImageUseCase.PROMPT_EMPTY, message)
        }
    }

    @Test
    fun generateImageUseCase_OnInput_generateImages() = runTest {
        val prompt = "anything of cat"
        val generatedImages = useCase(prompt)

        generatorRepository.sendPrompt(testImage)

        assertTrue(generatedImages.first().data.isNotEmpty())
    }
}

private val testImage = Image(
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
