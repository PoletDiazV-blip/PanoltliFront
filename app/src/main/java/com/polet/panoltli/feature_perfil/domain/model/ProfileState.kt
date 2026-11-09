package com.polet.panoltli.feature_perfil.domain.model

data class ProfileState(
    val isLoading: Boolean = false,
    val userProfile: UserProfile? = null,
    val isPasswordUpdating: Boolean = false,
    val passwordUpdateSuccess: Boolean = false,
    val errorMessage: String? = null,
)
