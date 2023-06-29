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

package com.tydev.imagegenerator.core.network.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.tydev.imagegenerator.core.model.data.Chat
import com.tydev.imagegenerator.core.model.data.Image
import com.tydev.imagegenerator.core.model.request.CompletionBody
import com.tydev.imagegenerator.core.model.request.GenerateImageBody
import com.tydev.imagegenerator.core.network.BuildConfig
import com.tydev.imagegenerator.core.network.NetworkDataSource
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Inject
import javax.inject.Singleton

private interface RetrofitNetworkApi {
    @POST(value = "v1/chat/completions")
    suspend fun getPrompt(
        @Body completion: CompletionBody,
    ): Chat

    @POST(value = "v1/images/generations")
    suspend fun generateImage(
        @Body generateImage: GenerateImageBody,
    ): Image
}

private const val BaseUrl = BuildConfig.BACKEND_URL

/**
 * [Retrofit] backed [NetworkDataSource]
 */
@Singleton
class RetrofitNetwork @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: OkHttpClient,
    private val networkInterceptor: NetworkInterceptor,
) : NetworkDataSource {

    private val networkApi: RetrofitNetworkApi by lazy {
        val clientWithInterceptor = okhttpCallFactory.newBuilder()
            .addInterceptor(networkInterceptor)
            .build()

        Retrofit.Builder()
            .baseUrl(BaseUrl)
            .client(clientWithInterceptor)
            .addConverterFactory(
                @OptIn(ExperimentalSerializationApi::class)
                networkJson.asConverterFactory("application/json".toMediaType()),
            )
            .build()
            .create(RetrofitNetworkApi::class.java)
    }

    override suspend fun getPrompt(completion: CompletionBody): Chat =
        networkApi.getPrompt(completion = completion)

    override suspend fun generateImage(generateImage: GenerateImageBody): Image =
        networkApi.generateImage(generateImage = generateImage)
}
