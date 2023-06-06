package com.tydev.core.imagegenerator.domain

import com.tydev.core.imagegenerator.testing.repository.TestGeneratorRepository
import com.tydev.core.imagegenerator.testing.util.MainDispatcherRule
import com.tydev.imageGenerator.core.model.data.Chat
import com.tydev.imageGenerator.core.model.data.Choice
import com.tydev.imageGenerator.core.model.data.Message
import com.tydev.imageGenerator.core.model.data.Usage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

class GetPromptUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val generatorRepository = TestGeneratorRepository()

    val useCase = GetPromptUseCase(generatorRepository)

    @Test
    fun getPromptUseCase_OnInput_getPrompt() = runTest {
        val word = "cat"
        val responsePrompt = useCase(word)

        generatorRepository.sendWord(testChat)

        assertTrue(responsePrompt.first().choices.first().message.content.isNotEmpty())
    }

}

private val testChat = Chat(
    id = "chatcmpl-123",
    objectValue = "chat.completion",
    created = 1,
    choices = listOf(
        Choice(
            index = 0,
            message = Message(
                role = "assistant",
                content = "anything of cat"
            ),
            finishReason = "stop"
        ),
    ),
    usage = Usage(
        promptTokens = 9,
        completionTokens = 12,
        totalTokens = 21
    )
)
