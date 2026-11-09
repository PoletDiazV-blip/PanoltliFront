package com.polet.panoltli.feature_historial.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polet.panoltli.feature_historial.domain.model.HistoryState
import com.polet.panoltli.feature_historial.domain.usecase.DeleteHistoryItemUseCase
import com.polet.panoltli.feature_historial.domain.usecase.GetHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel
    @Inject
    constructor(
        private val getHistoryUseCase: GetHistoryUseCase,
        private val deleteHistoryItemUseCase: DeleteHistoryItemUseCase,
    ) : ViewModel() {
        private val _state = MutableStateFlow(HistoryState())
        val state: StateFlow<HistoryState> = _state.asStateFlow()

        init {
            loadHistory()
        }

        fun loadHistory() {
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true, errorMessage = null) }
                val result = getHistoryUseCase()
                result.fold(
                    onSuccess = { items ->
                        _state.update { it.copy(isLoading = false, historyItems = items) }
                    },
                    onFailure = { error ->
                        _state.update { it.copy(isLoading = false, errorMessage = error.message ?: "Error al obtener historial") }
                    },
                )
            }
        }

        fun deleteItem(id: String) {
            viewModelScope.launch {
                val result = deleteHistoryItemUseCase(id)
                result.onSuccess {
                    // Actualizamos la lista local removiendo el elemento eliminado
                    _state.update { currentState ->
                        currentState.copy(
                            historyItems = currentState.historyItems.filter { it.id != id },
                        )
                    }
                }
            }
        }
    }
