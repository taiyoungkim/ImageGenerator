package com.tydev.core.imagegenerator.domain

import com.tydev.core.imagegenerator.testing.repository.TestUserDataRepository
import com.tydev.core.imagegenerator.testing.util.MainDispatcherRule
import com.tydev.imagegenerator.core.model.data.UserData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class GetUserDataUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val userDataRepository = TestUserDataRepository()

    val useCase = GetUserDataUseCase(userDataRepository)

    @Test
    fun getUserDataUseCase_OnInvoke_EmitsUserDataFromRepository() = runTest {
        val expectedUserData = UserData("testApiKey")
        userDataRepository.setUserData(expectedUserData)

        val userData = useCase().first()

        assertEquals(expectedUserData, userData)
    }
}