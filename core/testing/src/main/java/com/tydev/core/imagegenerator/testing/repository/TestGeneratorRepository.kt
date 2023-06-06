package com.tydev.core.imagegenerator.testing.repository

import com.tydev.imageGenerator.core.model.data.Chat
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

    override fun getPrompt(word: String): Flow<Chat> = promptFlow

    fun sendWord(chat: Chat) {
        promptFlow.tryEmit(chat)
    }
}