package com.polet.panoltli.feature_historial.data.repository

import com.polet.panoltli.feature_historial.data.remote.HistoryApi
import com.polet.panoltli.feature_historial.domain.model.HistoryItem
import javax.inject.Inject

class HistoryRepositoryImpl
    @Inject
    constructor(
        private val api: HistoryApi,
    ) {
        suspend fun getHistory(): Result<List<HistoryItem>> {
            return try {
                val response = api.getHistory()
                if (response.isSuccessful && response.body() != null) {
                    val list =
                        response.body()!!.map { dto ->
                            HistoryItem(
                                id = dto.id,
                                translatedText = dto.translatedText,
                                confidence = dto.confidence,
                                audioUrl = dto.audioUrl,
                                timestamp = dto.timestamp,
                            )
                        }
                    Result.success(list)
                } else {
                    Result.failure(Exception("Error al cargar el historial del servidor"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

        suspend fun deleteHistoryItem(id: String): Result<Unit> {
            return try {
                val response = api.deleteHistoryItem(id)
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("No se pudo eliminar el elemento del historial"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
