package com.polet.panoltli.feature_traductor.data.remote

import com.google.gson.annotations.SerializedName

// Lo que responde tu backend al procesar un fotograma de seña
data class TranslationResponseDto(
    @SerializedName("id") val id: String?,
    @SerializedName("translated_text") val translatedText: String,
    @SerializedName("confidence") val confidence: Float,
    @SerializedName("audio_url") val audioUrl: String?,
)
