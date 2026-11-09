package com.polet.panoltli.feature_diccionario.data.repository

import com.polet.panoltli.feature_diccionario.data.remote.DictionaryApi
import com.polet.panoltli.feature_diccionario.domain.model.SignItem
import javax.inject.Inject

class DictionaryRepositoryImpl
@Inject
constructor(
    private val api: DictionaryApi,
) {
    suspend fun getDictionarySigns(): Result<List<SignItem>> {
        return try {
            val response = api.getSigns()
            if (response.isSuccessful && response.body() != null) {
                val list =
                    response.body()!!.map { dto ->
                        SignItem(
                            id = dto.id,
                            name = dto.title, // Mapeamos el título del DTO al nombre del modelo
                            description = dto.description,
                            category = dto.category,
                            videoUrl = dto.videoUrl
                        )
                    }
                Result.success(list)
            } else {
                Result.failure(Exception("Error al obtener el diccionario del servidor"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}