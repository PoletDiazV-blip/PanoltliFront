package com.polet.panoltli.feature_traductor.domain.usecase

import com.polet.panoltli.feature_traductor.data.repository.TranslatorRepositoryImpl
import com.polet.panoltli.feature_traductor.domain.model.Translation
import javax.inject.Inject

class GetTranslationHistoryUseCase
    @Inject
    constructor(
        private val repository: TranslatorRepositoryImpl,
    ) {
        suspend operator fun invoke(): Result<List<Translation>> {
            return repository.getHistory()
        }
    }
