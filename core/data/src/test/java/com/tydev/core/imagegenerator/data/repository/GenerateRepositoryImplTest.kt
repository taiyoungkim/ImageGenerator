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

package com.tydev.core.imagegenerator.data.repository

import com.tydev.core.imagegenerator.data.testdoubles.TestNetworkDataSource
import com.tydev.imagegenerator.core.data.repository.GenerateRepositoryImpl
import com.tydev.imagegenerator.core.model.request.CompletionBody
import com.tydev.imagegenerator.core.model.request.GenerateImageBody
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class GenerateRepositoryImplTest {

    private lateinit var subject: GenerateRepositoryImpl

    private lateinit var network: TestNetworkDataSource

    @Before
    fun setup() {
        network = TestNetworkDataSource()

        subject = GenerateRepositoryImpl(
            networkDataSource = network,
            ioDispatcher = UnconfinedTestDispatcher(),
        )
    }

    @Test
    fun getPrompt_Emits_The_Expected_Result() = runTest {
        val word = "test"

        val result = subject.getPrompt(word).first()

        assertEquals(network.getPrompt(CompletionBody("", emptyList())), result)
    }

    @Test
    fun getPrompt_Throws_When_An_Error_Occurs() = runTest {
        val word = "error"

        val exception = assertFailsWith<IllegalArgumentException> {
            subject.getPrompt(word).first()
        }

        assertEquals("404", exception.message)
    }

    @Test
    fun generateImages_Emits_The_Expected_Result() = runTest {
        val prompt = "test"

        val result = subject.generateImages(prompt).first()

        assertEquals(network.generateImage(GenerateImageBody("", 0, "")), result)
    }

    @Test
    fun generateImages_Throws_When_An_Error_Occurs() = runTest {
        val prompt = "error"

        val exception = assertFailsWith<IllegalArgumentException> {
            subject.generateImages(prompt).first()
        }

        assertEquals("404", exception.message)
    }
}
