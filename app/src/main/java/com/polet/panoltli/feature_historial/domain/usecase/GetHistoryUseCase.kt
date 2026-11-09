package com.polet.panoltli.feature_historial.domain.usecase

import com.polet.panoltli.feature_historial.data.repository.HistoryRepositoryImpl
import com.polet.panoltli.feature_historial.domain.model.HistoryItem
import javax.inject.Inject

class GetHistoryUseCase
    @Inject
    constructor(
        private val repository: HistoryRepositoryImpl,
    ) {
        suspend operator fun invoke(): Result<List<HistoryItem>> {
            return repository.getHistory()
        }
    }
