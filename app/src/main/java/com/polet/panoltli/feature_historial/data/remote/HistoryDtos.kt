package com.polet.panoltli.feature_historial.data.remote

import com.google.gson.annotations.SerializedName

data class HistoryItemDto(
    @SerializedName("id") val id: String,
    @SerializedName("translated_text") val translatedText: String,
    @SerializedName("confidence") val confidence: Float,
    @SerializedName("audio_url") val audioUrl: String?,
    @SerializedName("timestamp") val timestamp: String,
)
