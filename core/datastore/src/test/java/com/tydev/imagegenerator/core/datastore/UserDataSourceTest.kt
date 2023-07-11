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

package com.tydev.imagegenerator.core.datastore

import com.tydev.imagegenerator.core.datastore.test.testUserPreferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
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
    fun saveApiKey_On_User_Input_Saves_Key_To_Data_Store() = testScope.runTest {
        val expectApiKey = "yourApiKey"

        subject.setApiKey(expectApiKey)
        val actualApiKey = subject.userData.first().apiKey

        assertEquals(expectApiKey, actualApiKey)
    }

    @Test
    fun setApiKey_On_IO_Exception_Shows_Error_Log() = testScope.runTest {
    }
}
