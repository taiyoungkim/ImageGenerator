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

package com.tydev.core.imagegenerator.data.repository

import com.tydev.imagegenerator.core.data.repository.UserDataRepositoryImpl
import com.tydev.imagegenerator.core.datastore.UserDataSource
import com.tydev.imagegenerator.core.datastore.test.testUserPreferencesDataStore
import com.tydev.imagegenerator.core.model.data.UserData
import com.tydev.imagegenerator.core.network.retrofit.NetworkInterceptor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class UserDataRepositoryImplTest {
    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var subject: UserDataRepositoryImpl

    private lateinit var userDataSource: UserDataSource

    private lateinit var networkInterceptor: NetworkInterceptor

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @Before
    fun setup() {
        userDataSource = UserDataSource(
            tmpFolder.testUserPreferencesDataStore(testScope),
        )

        networkInterceptor = NetworkInterceptor()

        subject = UserDataRepositoryImpl(
            preferencesDataSource = userDataSource,
            networkInterceptor = networkInterceptor,
        )
    }

    @Test
    fun userData_Should_Return_Flow_From_UserDataSource() = runTest {
        val expectedApiKey = "testApiKey"
        userDataSource.setApiKey(expectedApiKey)

        val actualUserData = subject.userData.first()

        assertEquals(expectedApiKey, actualUserData.apiKey)
    }

    @Test
    fun fetch_User_Data_And_Update_Key_Should_Update_ApiKey_In_NetworkInterceptor() = runTest {
        val testUserData = UserData(apiKey = "testApiKey")

        subject.fetchUserDataAndUpdateKey(testUserData)

        assertEquals(testUserData.apiKey, networkInterceptor.apiKey)
    }
}
