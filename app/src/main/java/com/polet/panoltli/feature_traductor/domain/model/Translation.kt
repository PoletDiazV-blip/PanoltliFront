package com.polet.panoltli.feature_traductor.domain.model

data class Translation(
    val id: String?,
    val translatedText: String,
    val confidence: Float,
    val audioUrl: String?,
    val timestamp: Long = System.currentTimeMillis(),
)
