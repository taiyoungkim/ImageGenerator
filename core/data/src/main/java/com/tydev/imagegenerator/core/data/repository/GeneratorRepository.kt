package com.tydev.imagegenerator.core.data.repository

import com.tydev.imageGenerator.core.model.data.Chat
import com.tydev.imageGenerator.core.model.data.Image
import kotlinx.coroutines.flow.Flow

interface GeneratorRepository {

    /**
     * Get the prompt from gpt
     */
    fun getPrompt(word: String): Flow<Chat>

    /**
     * Get the image to use prompt
     */
    fun generateImages(prompt: String): Flow<Image>
}
