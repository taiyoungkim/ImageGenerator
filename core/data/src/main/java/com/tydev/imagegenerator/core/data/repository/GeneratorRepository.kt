package com.tydev.imagegenerator.core.data.repository

import com.tydev.imageGenerator.core.model.data.Chat
import kotlinx.coroutines.flow.Flow

interface GeneratorRepository {

    /**
     * Get the prompt from gpt
     */
    fun getPrompt(word: String): Flow<Chat>
}
