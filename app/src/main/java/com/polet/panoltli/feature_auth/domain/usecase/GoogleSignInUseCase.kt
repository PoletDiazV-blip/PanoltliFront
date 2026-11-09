package com.polet.panoltli.feature_auth.domain.usecase

import com.polet.panoltli.feature_auth.data.dto.LoginResponseDto
import com.polet.panoltli.feature_auth.data.repository.AuthRepository
import javax.inject.Inject

class GoogleSignInUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(idToken: String): Result<LoginResponseDto> {
        return repository.googleSignIn(idToken)
    }
}