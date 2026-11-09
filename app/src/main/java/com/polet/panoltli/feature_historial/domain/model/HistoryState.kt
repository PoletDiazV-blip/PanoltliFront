package com.polet.panoltli.feature_historial.domain.model

data class HistoryState(
    val isLoading: Boolean = false,
    val historyItems: List<HistoryItem> = emptyList(),
    val errorMessage: String? = null,
)
