package com.polet.panoltli.feature_historial.domain.usecase

import com.polet.panoltli.feature_historial.data.repository.HistoryRepositoryImpl
import javax.inject.Inject

class DeleteHistoryItemUseCase
    @Inject
    constructor(
        private val repository: HistoryRepositoryImpl,
    ) {
        suspend operator fun invoke(id: String): Result<Unit> {
            return repository.deleteHistoryItem(id)
        }
    }
