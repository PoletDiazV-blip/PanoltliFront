package com.polet.panoltli.feature_auth.data.dto

data class LoginResponseDto(
    val token: String,
    val name: String,
    val email: String
)
