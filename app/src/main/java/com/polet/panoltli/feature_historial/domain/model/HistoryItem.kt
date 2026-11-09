package com.polet.panoltli.feature_historial.domain.model

data class HistoryItem(
    val id: String,
    val translatedText: String,
    val confidence: Float,
    val audioUrl: String?,
    val timestamp: String,
)
