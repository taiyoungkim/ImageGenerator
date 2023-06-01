package com.tydev.imagegenerator.core.datastore

import com.tydev.imagegenerator.core.datastore.test.testUserPreferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class UserDataSourceTest {

    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var subject: UserDataSource

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @Before
    fun setup() {
        subject = UserDataSource(
            tmpFolder.testUserPreferencesDataStore(testScope),
        )
    }

    @Test
    fun saveApiKey_OnUserInput_SavesKeyToDataStore() = testScope.runTest {
        // Arrange
        val expectApiKey = "yourApiKey"

        // When
        subject.setApiKey(expectApiKey)
        val actualApiKey = subject.userData.first().apiKey

        // Then
        kotlin.test.assertEquals(expectApiKey, actualApiKey)
    }
}