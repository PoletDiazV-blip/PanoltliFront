package com.polet.panoltli.feature_perfil.domain.usecase

import com.polet.panoltli.feature_perfil.data.repository.ProfileRepositoryImpl
import com.polet.panoltli.feature_perfil.domain.model.UserProfile
import javax.inject.Inject

class GetUserProfileUseCase
    @Inject
    constructor(
        private val repository: ProfileRepositoryImpl,
    ) {
        suspend operator fun invoke(): Result<UserProfile> {
            return repository.getUserProfile()
        }
    }
