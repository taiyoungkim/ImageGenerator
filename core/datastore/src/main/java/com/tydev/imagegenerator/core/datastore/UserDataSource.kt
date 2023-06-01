package com.tydev.imagegenerator.core.datastore

import androidx.datastore.core.DataStore
import com.tydev.imageGenerator.core.model.data.UserData
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {
    val userData = userPreferences.data.map {
        UserData(
            apiKey = it.apiKey
        )
    }

    suspend fun setApiKey(apiKey: String) {
        userPreferences.updateData {
            it.copy {
                this.apiKey = apiKey
            }
        }
    }
}