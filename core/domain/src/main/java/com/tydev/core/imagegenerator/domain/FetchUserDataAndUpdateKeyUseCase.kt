package com.tydev.core.imagegenerator.domain

import com.tydev.imagegenerator.core.data.repository.UserDataRepository
import com.tydev.imagegenerator.core.model.data.UserData
import javax.inject.Inject

class FetchUserDataAndUpdateKeyUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository,
) {
    suspend operator fun invoke(apiKey: String) {
        userDataRepository.fetchUserDataAndUpdateKey(apiKey)
    }
}