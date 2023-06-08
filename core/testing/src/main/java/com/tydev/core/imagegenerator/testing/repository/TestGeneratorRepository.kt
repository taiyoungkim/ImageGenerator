package com.tydev.core.imagegenerator.testing.repository

import com.tydev.imageGenerator.core.model.data.Chat
import com.tydev.imageGenerator.core.model.data.Image
import com.tydev.imagegenerator.core.data.repository.GeneratorRepository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class TestGeneratorRepository: GeneratorRepository {
    /**
     * The backing hot flow for the prompt for testing.
     */
    private val promptFlow: MutableSharedFlow<Chat> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    /**
     * The backing hot flow for the image for testing.
     */
    private val imagesFlow: MutableSharedFlow<Image> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun getPrompt(word: String): Flow<Chat> = promptFlow
    override fun generateImages(prompt: String): Flow<Image> = imagesFlow

    fun sendWord(chat: Chat) {
        promptFlow.tryEmit(chat)
    }

    fun sendPrompt(images: Image) {
        imagesFlow.tryEmit(images)
    }
}