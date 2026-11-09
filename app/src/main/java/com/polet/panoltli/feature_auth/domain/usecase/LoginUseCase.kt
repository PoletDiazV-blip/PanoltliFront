package com.polet.panoltli.feature_auth.domain.usecase

import com.polet.panoltli.feature_auth.data.repository.AuthRepositoryImpl
import javax.inject.Inject

class LoginUseCase
@Inject
constructor(
    private val repository: AuthRepositoryImpl,
) {
    // El operador 'invoke' permite llamar a la clase como si fuera una función: loginUseCase(email, pass)
    suspend operator fun invoke(
        email: String,
        pass: String,
    ): Result<Unit> {
        // Validaciones básicas de negocio antes de golpear la red
        if (email.isBlank() || pass.isBlank()) {
            return Result.failure(IllegalArgumentException("El correo y la contraseña no pueden estar vacíos"))
        }

        // Mapeamos el Result<Boolean> del repositorio a Result<Unit> para cumplir con la firma
        return repository.login(email, pass).map {}
    }
}