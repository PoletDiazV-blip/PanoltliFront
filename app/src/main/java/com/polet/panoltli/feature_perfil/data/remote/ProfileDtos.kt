package com.polet.panoltli.feature_perfil.data.remote

import com.google.gson.annotations.SerializedName

data class UserProfileDto(
    @SerializedName("id") val id: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("profile_picture_url") val profilePictureUrl: String?,
)

data class UpdatePasswordRequestDto(
    @SerializedName("old_password") val oldPassword: String,
    @SerializedName("new_password") val newPassword: String,
)
