package com.polet.panoltli.feature_auth.presentation

data class AuthState(
    val isLoginTab: Boolean = true,
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isTwoFactorActive: Boolean = false,
    val otpCode: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isAuthenticated: Boolean = false
)