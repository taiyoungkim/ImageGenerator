/*
 * Copyright 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tydev.imagegenerator

import com.tydev.core.imagegenerator.testing.repository.TestUserDataRepository
import com.tydev.core.imagegenerator.testing.util.MainDispatcherRule
import com.tydev.imagegenerator.core.datastore.di.DataStoreModule
import com.tydev.imagegenerator.core.model.data.UserData
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(DataStoreModule::class)
class MainViewModelTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val userDataRepository = TestUserDataRepository()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        viewModel = MainViewModel(userDataRepository)
    }

    @Test
    fun state_Is_Success_When_api_Key_Get() = runTest {
        val userData = UserData(apiKey = "validApiKey")
        userDataRepository.setUserData(userData)

        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        val uiState = viewModel.uiState.value

        assertTrue(uiState is MainActivityUiState.Success)
        assertEquals(userData, (uiState as MainActivityUiState.Success).userData)

        collectJob.cancel()
    }
}
