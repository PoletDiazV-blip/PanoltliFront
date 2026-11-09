package com.polet.panoltli.feature_auth.domain.usecase

import com.polet.panoltli.feature_auth.data.repository.AuthRepositoryImpl
import javax.inject.Inject

class RegisterUseCase
@Inject
constructor(
    private val repository: AuthRepositoryImpl,
) {
    suspend operator fun invoke(
        name: String,
        email: String,
        pass: String,
    ): Result<Unit> {
        if (name.isBlank() || email.isBlank() || pass.isBlank()) {
            return Result.failure(IllegalArgumentException("Todos los campos son obligatorios"))
        }
        // Mapeamos el Result<Boolean> a Result<Unit>
        return repository.register(name, email, pass).map {}
    }
}