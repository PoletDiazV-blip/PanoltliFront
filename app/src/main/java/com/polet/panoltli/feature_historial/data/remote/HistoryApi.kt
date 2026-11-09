package com.polet.panoltli.feature_historial.data.remote

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface HistoryApi {
    @GET("api/v1/history")
    suspend fun getHistory(): Response<List<HistoryItemDto>>

    @DELETE("api/v1/history/{id}")
    suspend fun deleteHistoryItem(
        @Path("id") id: String,
    ): Response<Unit>
}