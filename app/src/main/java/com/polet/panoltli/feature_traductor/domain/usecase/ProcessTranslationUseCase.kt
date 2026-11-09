package com.polet.panoltli.feature_traductor.domain.usecase

import com.polet.panoltli.feature_traductor.data.repository.TranslatorRepositoryImpl
import com.polet.panoltli.feature_traductor.domain.model.Translation
import javax.inject.Inject

class ProcessTranslationUseCase
    @Inject
    constructor(
        private val repository: TranslatorRepositoryImpl,
    ) {
        suspend operator fun invoke(frameData: ByteArray): Result<Translation> {
            if (frameData.isEmpty()) {
                return Result.failure(IllegalArgumentException("El fotograma de video está vacío"))
            }
            return repository.processFrame(frameData)
        }
    }
