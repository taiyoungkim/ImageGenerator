package com.tydev.core.imagegenerator.domain

import com.tydev.imageGenerator.core.model.data.Image
import com.tydev.imagegenerator.core.data.repository.GeneratorRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GenerateImageUseCase @Inject constructor(
    private val generatorRepository: GeneratorRepository
) {

    operator fun invoke(prompt: String): Flow<Image> {
        if (prompt.isEmpty()) {
            throw IllegalArgumentException("Prompt must not be null or empty")
        }

        return generatorRepository.generateImages(prompt)
    }
}