package com.polet.panoltli.feature_perfil.domain.usecase

import com.polet.panoltli.feature_perfil.data.repository.ProfileRepositoryImpl
import javax.inject.Inject

class UpdatePasswordUseCase
    @Inject
    constructor(
        private val repository: ProfileRepositoryImpl,
    ) {
        suspend operator fun invoke(
            oldPass: String,
            newPass: String,
        ): Result<Unit> {
            if (oldPass.isBlank() || newPass.isBlank()) {
                return Result.failure(IllegalArgumentException("Las contraseñas no pueden estar vacías"))
            }
            return repository.updatePassword(oldPass, newPass)
        }
    }
