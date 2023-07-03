package com.tydev.core.imagegenerator.data.repository

import com.tydev.core.imagegenerator.data.testdoubles.TestNetworkDataSource
import com.tydev.imagegenerator.core.data.repository.GenerateRepositoryImpl
import com.tydev.imagegenerator.core.model.request.CompletionBody
import com.tydev.imagegenerator.core.model.request.GenerateImageBody
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class GenerateRepositoryImplTest {

    private lateinit var subject: GenerateRepositoryImpl

    private lateinit var network: TestNetworkDataSource

    @Before
    fun setup() {
        network = TestNetworkDataSource()

        subject = GenerateRepositoryImpl(
            networkDataSource = network,
            ioDispatcher = UnconfinedTestDispatcher(),
        )
    }

    @Test
    fun getPrompt_Emits_The_Expected_Result() = runTest {
        val word = "test"

        val result = subject.getPrompt(word).first()

        assertEquals(network.getPrompt(CompletionBody("", emptyList())), result)
    }

    @Test
    fun getPrompt_Throws_When_An_Error_Occurs() = runTest {
        val word = "error"

        val exception = assertFailsWith<IllegalArgumentException> {
            subject.getPrompt(word).first()
        }

        assertEquals("404", exception.message)
    }

    @Test
    fun generateImages_Emits_The_Expected_Result() = runTest {
        val prompt = "test"

        val result = subject.generateImages(prompt).first()

        assertEquals(network.generateImage(GenerateImageBody("", 0, "")), result)
    }

    @Test
    fun generateImages_Throws_When_An_Error_Occurs() = runTest {
        val prompt = "error"

        val exception = assertFailsWith<IllegalArgumentException> {
            subject.generateImages(prompt).first()
        }

        assertEquals("404", exception.message)
    }
}
