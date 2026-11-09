package com.polet.panoltli.feature_auth.data.repository

import com.polet.panoltli.feature_auth.data.dto.LoginResponseDto

interface AuthRepository {
    suspend fun register(name: String, email: String, password: String): Result<String>
    suspend fun verifyOtp(email: String, otp: String): Result<LoginResponseDto>
    suspend fun login(email: String, password: String): Result<LoginResponseDto>
    suspend fun googleSignIn(idToken: String): Result<LoginResponseDto>
}