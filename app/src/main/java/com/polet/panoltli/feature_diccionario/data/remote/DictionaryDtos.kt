package com.polet.panoltli.feature_diccionario.data.remote

import com.google.gson.annotations.SerializedName

data class SignDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("category") val category: String,
    @SerializedName("video_url") val videoUrl: String?,
    @SerializedName("image_url") val imageUrl: String?,
)
