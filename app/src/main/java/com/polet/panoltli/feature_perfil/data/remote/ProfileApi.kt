package com.polet.panoltli.feature_perfil.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface ProfileApi {
    @GET("api/v1/profile")
    suspend fun getUserProfile(): Response<UserProfileDto>

    @PUT("api/v1/profile/password")
    suspend fun updatePassword(
        @Body body: UpdatePasswordRequestDto,
    ): Response<Unit>
}
