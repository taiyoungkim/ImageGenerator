package com.tydev.imagegenerator.core.datastore

import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import kotlin.test.assertTrue

class ApiKeyDataSourceTest {

    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var subject: ApiKeyDataSource

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @Before
    fun setup() {
        subject = ApiKeyDataSource(
            tmpFolder.testApiKeyDataStore(testScope),
        )
    }

    @Test
    fun saveApiKey_OnUserInput_SavesKeyToDataStore() = testScope.runTest {
        // Arrange
        val userInput = "yourApiKey"

        // When
        subject.setApiKey(userInput)

        // Then
        assertTrue(subject.apiKey)
    }
}