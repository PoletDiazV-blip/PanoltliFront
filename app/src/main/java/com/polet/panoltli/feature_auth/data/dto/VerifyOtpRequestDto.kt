package com.polet.panoltli.feature_auth.data.dto

data class VerifyOtpRequestDto(
    val email: String,
    val otp: String
)
