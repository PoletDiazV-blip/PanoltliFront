package com.polet.panoltli.feature_auth.data.remote

import com.polet.panoltli.feature_auth.data.dto.LoginRequestDto
import com.polet.panoltli.feature_auth.data.dto.LoginResponseDto
import com.polet.panoltli.feature_auth.data.dto.RegisterRequestDto
import com.polet.panoltli.feature_auth.data.dto.VerifyOtpRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import com.polet.panoltli.feature_auth.data.dto.GoogleLoginRequestDto

interface AuthApi {
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequestDto): Response<String>

    @POST("api/auth/verify-otp")
    suspend fun verifyOtp(@Body request: VerifyOtpRequestDto): Response<LoginResponseDto>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequestDto): Response<LoginResponseDto>

    @POST("api/auth/google")
    suspend fun googleSignIn(@Body request: GoogleLoginRequestDto): Response<LoginResponseDto>
}