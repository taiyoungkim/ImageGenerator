package com.tydev.core.imagegenerator.domain

import com.tydev.core.imagegenerator.testing.repository.TestGeneratorRepository
import com.tydev.core.imagegenerator.testing.util.MainDispatcherRule
import com.tydev.imageGenerator.core.model.data.DataItem
import com.tydev.imageGenerator.core.model.data.Image
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

class GenerateImageUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val generatorRepository = TestGeneratorRepository()

    val useCase = GenerateImageUseCase(generatorRepository)

    @Test
    fun generateImageUseCase_OnInput_generateImages() = runTest {
        val prompt = "anything of cat"
        val generatedImages = useCase(prompt)

        generatorRepository.sendPrompt(testImage)

        assertTrue(generatedImages.first().data.isNotEmpty())
    }
}

private val testImage = Image(
    created = 1589478378,
    data = listOf(
        DataItem(
            url = "url1"
        ),
        DataItem(
            url = "url2"
        ),
        DataItem(
            url = "url3"
        ),
        DataItem(
            url = "url4"
        )
    )
)
