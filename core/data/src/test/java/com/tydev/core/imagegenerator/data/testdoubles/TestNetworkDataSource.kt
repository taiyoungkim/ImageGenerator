package com.tydev.core.imagegenerator.data.testdoubles

import com.tydev.imagegenerator.core.model.data.Chat
import com.tydev.imagegenerator.core.model.data.Image
import com.tydev.imagegenerator.core.model.request.CompletionBody
import com.tydev.imagegenerator.core.model.request.GenerateImageBody
import com.tydev.imagegenerator.core.network.NetworkDataSource
import com.tydev.imagegenerator.core.network.fake.FakeNetworkDataSource
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher

/**
 * Test double for [NetworkDataSource]
 */
class TestNetworkDataSource : NetworkDataSource {
    private val source = FakeNetworkDataSource(UnconfinedTestDispatcher())

    private val allPrompts = runBlocking { source.getPrompt(CompletionBody("", emptyList())) }

    private val allImages = runBlocking { source.generateImage(GenerateImageBody("", 0, "")) }

    override suspend fun getPrompt(completion: CompletionBody): Chat {
        return if (completion.messages.any { it.content == "error" }) {
            throw IllegalArgumentException("Test error")
        } else {
            allPrompts
        }
    }

    override suspend fun generateImage(generateImage: GenerateImageBody): Image {
        return if (generateImage.prompt == "error") {
            throw IllegalArgumentException("Test error")
        } else {
            allImages
        }
    }
}
