package com.tydev.core.imagegenerator.domain

import com.tydev.core.imagegenerator.testing.repository.TestUserDataRepository
import com.tydev.core.imagegenerator.testing.util.MainDispatcherRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class FetchUserDataAndUpdateKeyUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val userDataRepository = TestUserDataRepository()

    val useCase = FetchUserDataAndUpdateKeyUseCase(userDataRepository)

    @Test
    fun fetchUserDataAndUpdateKeyUseCase_OnExecute_UpdatesApiKey() = runTest {
        val apiKey = "testApiKey"

        useCase(apiKey)

        assertEquals(apiKey, userDataRepository.userData.first().apiKey)
    }
}