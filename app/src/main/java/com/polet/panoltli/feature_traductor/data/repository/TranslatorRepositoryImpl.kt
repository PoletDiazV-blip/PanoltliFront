package com.polet.panoltli.feature_traductor.data.repository

import android.util.Base64
import com.polet.panoltli.feature_traductor.data.remote.TranslatorApi
import com.polet.panoltli.feature_traductor.domain.model.Translation
import javax.inject.Inject

class TranslatorRepositoryImpl
    @Inject
    constructor(
        private val api: TranslatorApi,
    ) {
        suspend fun processFrame(frameData: ByteArray): Result<Translation> {
            return try {
                // Convertimos los bytes de la cámara a Base64 para enviarlos de forma segura por JSON (o ajusta según tu API)
                val base64Image = Base64.encodeToString(frameData, Base64.DEFAULT)
                val response = api.processFrame(mapOf("image" to base64Image))

                if (response.isSuccessful && response.body() != null) {
                    val dto = response.body()!!
                    Result.success(
                        Translation(
                            id = dto.id,
                            translatedText = dto.translatedText,
                            confidence = dto.confidence,
                            audioUrl = dto.audioUrl,
                        ),
                    )
                } else {
                    Result.failure(Exception("Error en el servidor al procesar la seña"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

        suspend fun getHistory(): Result<List<Translation>> {
            return try {
                val response = api.getHistory()
                if (response.isSuccessful && response.body() != null) {
                    val dtos = response.body()!!
                    val domainList =
                        dtos.map { dto ->
                            Translation(
                                id = dto.id,
                                translatedText = dto.translatedText,
                                confidence = dto.confidence,
                                audioUrl = dto.audioUrl,
                            )
                        }
                    Result.success(domainList)
                } else {
                    Result.failure(Exception("No se pudo obtener el historial"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
