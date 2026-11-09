package com.polet.panoltli.feature_diccionario.domain.model

data class SignItem(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    val videoUrl: String? = null
)