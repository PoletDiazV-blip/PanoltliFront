package com.polet.panoltli.feature_diccionario.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface DictionaryApi {
    @GET("api/v1/dictionary/signs")
    suspend fun getSigns(): Response<List<SignDto>>
}
