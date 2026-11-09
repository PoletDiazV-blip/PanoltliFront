package com.polet.panoltli.feature_traductor.domain.model

data class TranslatorState(
    val isTranslating: Boolean = false,
    val currentSign: String = "Esperando seña...",
    val confidence: Float = 0.0f,
    val audioUrl: String? = null,
    val errorMessage: String? = null,
    val recentTranslations: List<Translation> = emptyList(),
)
