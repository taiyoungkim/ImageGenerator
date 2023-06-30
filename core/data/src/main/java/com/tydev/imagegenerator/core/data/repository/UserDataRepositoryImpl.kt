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

package com.tydev.imagegenerator.core.data.repository

import com.tydev.imagegenerator.core.datastore.UserDataSource
import com.tydev.imagegenerator.core.model.data.UserData
import com.tydev.imagegenerator.core.network.Dispatcher
import com.tydev.imagegenerator.core.network.GeneratorAppDispatchers
import com.tydev.imagegenerator.core.network.retrofit.NetworkInterceptor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserDataRepositoryImpl @Inject constructor(
    preferencesDataSource: UserDataSource,
    private val networkInterceptor: NetworkInterceptor,
) : UserDataRepository {

    override val userData: Flow<UserData> = preferencesDataSource.userData

    override suspend fun fetchUserDataAndUpdateKey(userData: UserData) {
        networkInterceptor.apiKey = userData.apiKey
    }
}
