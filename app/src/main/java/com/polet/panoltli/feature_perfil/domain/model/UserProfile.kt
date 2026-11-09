package com.polet.panoltli.feature_perfil.domain.model

data class UserProfile(
    val id: String,
    val username: String,
    val email: String,
    val profilePictureUrl: String?,
)
