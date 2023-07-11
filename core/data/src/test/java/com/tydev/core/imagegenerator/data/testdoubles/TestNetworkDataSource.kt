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

package com.tydev.core.imagegenerator.data.testdoubles

import com.tydev.imagegenerator.core.model.data.Chat
import com.tydev.imagegenerator.core.model.data.Image
import com.tydev.imagegenerator.core.model.request.CompletionBody
import com.tydev.imagegenerator.core.model.request.GenerateImageBody
import com.tydev.imagegenerator.core.network.NetworkDataSource
import com.tydev.imagegenerator.core.network.fake.FakeNetworkDataSource
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response

/**
 * Test double for [NetworkDataSource]
 */
class TestNetworkDataSource : NetworkDataSource {
    private val source = FakeNetworkDataSource(UnconfinedTestDispatcher())

    private val allPrompts = runBlocking { source.getPrompt(CompletionBody("", emptyList())) }

    private val allImages = runBlocking { source.generateImage(GenerateImageBody("", 0, "")) }

    override suspend fun getPrompt(completion: CompletionBody): Chat {
        if (completion.messages.any { it.content == "error" }) {
            throw HttpException(Response.error<Nothing>(404, "".toResponseBody(null)))
        }
        return allPrompts
    }

    override suspend fun generateImage(generateImage: GenerateImageBody): Image {
        if (generateImage.prompt == "error") {
            throw HttpException(Response.error<Nothing>(404, "".toResponseBody(null)))
        }
        return allImages
    }
}
