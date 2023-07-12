package com.tydev.core.imagegenerator.domain

import com.tydev.imagegenerator.core.data.repository.UserDataRepository
import com.tydev.imagegenerator.core.model.data.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository,
) {
    operator fun invoke(): Flow<UserData> = userDataRepository.userData
}