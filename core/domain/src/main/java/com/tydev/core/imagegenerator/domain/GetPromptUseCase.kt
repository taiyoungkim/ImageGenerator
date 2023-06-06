package com.tydev.core.imagegenerator.domain

import com.tydev.imageGenerator.core.model.data.Chat
import com.tydev.imagegenerator.core.data.repository.GeneratorRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPromptUseCase @Inject constructor(
    private val generatorRepository: GeneratorRepository
) {
    operator fun invoke(word: String): Flow<Chat> {
        if (word.isEmpty()) {
            throw IllegalArgumentException("Word must not be null or empty")
        } else if (word.length > 10) {
            throw IllegalArgumentException("Word over 10 characters")
        }

        return generatorRepository.getPrompt(word)
    }
}
