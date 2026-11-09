package com.polet.panoltli.feature_auth.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val token: String? = null
)