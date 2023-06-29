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

package com.tydev.imagegenerator.core.data.repository

import androidx.annotation.WorkerThread
import com.tydev.imagegenerator.core.model.data.Message
import com.tydev.imagegenerator.core.model.data.Role
import com.tydev.imagegenerator.core.model.request.CompletionBody
import com.tydev.imagegenerator.core.model.request.GenerateImageBody
import com.tydev.imagegenerator.core.network.Dispatcher
import com.tydev.imagegenerator.core.network.GeneratorAppDispatchers
import com.tydev.imagegenerator.core.network.NetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import javax.inject.Inject

class GenerateRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    @Dispatcher(GeneratorAppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : GeneratorRepository {

    @WorkerThread
    override fun getPrompt(word: String) = flow {
        val completion = CompletionBody(
            model = GPT_MODEL,
            messages = listOf(
                Message(
                    role = Role.SYSTEM.name.lowercase(),
                    content = PRE_ORDER_ROLE,
                ),
                Message(
                    role = Role.USER.name.lowercase(),
                    content = word,
                ),
            ),
        )

        try {
            emit(networkDataSource.getPrompt(completion))
        } catch (e: HttpException) {
            throw IllegalArgumentException(e.code().toString())
        } catch (e: Exception) {
            throw IllegalArgumentException(e)
        }

    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun generateImages(prompt: String) = flow {
        val generateImage = GenerateImageBody(
            n = DEFAULT_N,
            prompt = prompt,
            size = DEFAULT_SIZE,
        )

        try {
            emit(networkDataSource.generateImage(generateImage))
        } catch (e: HttpException) {
            throw IllegalArgumentException(e.code().toString())
        } catch (e: Exception) {
            throw IllegalArgumentException(e)
        }
    }.flowOn(ioDispatcher)

    companion object {
        const val GPT_MODEL = "gpt-3.5-turbo"
        const val PRE_ORDER_ROLE =
            "Imagine the detail appearance of the input. Response it shortly."
        const val DEFAULT_N = 4
        const val DEFAULT_SIZE = "256x256"
    }
}
