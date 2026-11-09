package com.polet.panoltli.feature_auth.data.remote

import com.google.gson.annotations.SerializedName

// --- Request DTOs ---

data class LoginRequestDto(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class RegisterRequestDto(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

// --- Response DTOs ---

data class LoginResponseDto(
    @SerializedName("token") val token: String,
    @SerializedName("type") val type: String? = "Bearer"
)

data class UserResponseDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("roles") val roles: List<String>
)