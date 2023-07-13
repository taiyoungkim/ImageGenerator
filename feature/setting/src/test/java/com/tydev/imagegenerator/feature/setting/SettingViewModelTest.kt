package com.tydev.imagegenerator.feature.setting

import com.tydev.core.imagegenerator.domain.FetchUserDataAndUpdateKeyUseCase
import com.tydev.core.imagegenerator.domain.GetUserDataUseCase
import com.tydev.core.imagegenerator.testing.repository.TestUserDataRepository
import com.tydev.core.imagegenerator.testing.util.MainDispatcherRule
import com.tydev.imagegenerator.core.model.data.UserData
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SettingViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testRepository = TestUserDataRepository()
    private val getUserDataUseCase = GetUserDataUseCase(testRepository)
    private val fetchUserDataAndUpdateKeyUseCase = FetchUserDataAndUpdateKeyUseCase(testRepository)

    private lateinit var viewModel: SettingViewModel

    @Before
    fun setup() {
        viewModel = SettingViewModel(getUserDataUseCase, fetchUserDataAndUpdateKeyUseCase)
    }

    @Test
    fun settingViewModel_OnInit_EmitsLoadingThenSuccess() = runTest {
        val expectedUserData = UserData(apiKey = "apiKey")
        val expectedState2 = SettingUiState.Success(expectedUserData)
        testRepository.setUserData(expectedUserData)

        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        val uiState = viewModel.uiState.value

        assertEquals(expectedState2, uiState)
        collectJob.cancel()
    }

    @Test
    fun settingViewModel_OnSetApiKey_UpdatesApiKey() = runTest {
        val newApiKey = "newApiKey"

        viewModel.setApiKey(newApiKey)

        val actualUserData = getUserDataUseCase().first()
        assertEquals(newApiKey, actualUserData.apiKey)
    }
}

