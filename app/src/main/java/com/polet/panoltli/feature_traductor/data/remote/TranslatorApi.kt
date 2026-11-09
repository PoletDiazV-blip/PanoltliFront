package com.polet.panoltli.feature_traductor.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TranslatorApi {
    @POST("api/v1/translations/process")
    suspend fun processFrame(
        @Body body: Map<String, String>, // O el payload binario/JSON correspondiente a tu API
    ): Response<TranslationResponseDto>

    @GET("api/v1/translations/history")
    suspend fun getHistory(): Response<List<TranslationResponseDto>>
}
