package com.polet.panoltli.feature_traductor.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polet.panoltli.feature_traductor.domain.model.TranslatorState
import com.polet.panoltli.feature_traductor.domain.usecase.GetTranslationHistoryUseCase
import com.polet.panoltli.feature_traductor.domain.usecase.ProcessTranslationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TranslatorViewModel
    @Inject
    constructor(
        private val processTranslationUseCase: ProcessTranslationUseCase,
        private val getHistoryUseCase: GetTranslationHistoryUseCase,
    ) : ViewModel() {
        private val _state = MutableStateFlow(TranslatorState())
        val state: StateFlow<TranslatorState> = _state.asStateFlow()

        init {
            loadHistory()
        }

        // Simula o procesa un fotograma capturado por la cámara
        fun onFrameCaptured(frameBytes: ByteArray) {
            viewModelScope.launch {
                _state.update { it.copy(isTranslating = true, errorMessage = null) }
                val result = processTranslationUseCase(frameBytes)
                result.fold(
                    onSuccess = { translation ->
                        _state.update { currentState ->
                            currentState.copy(
                                isTranslating = false,
                                currentSign = translation.translatedText,
                                confidence = translation.confidence,
                                audioUrl = translation.audioUrl,
                                recentTranslations = listOf(translation) + currentState.recentTranslations,
                            )
                        }
                    },
                    onFailure = { error ->
                        _state.update { it.copy(isTranslating = false, errorMessage = error.message) }
                    },
                )
            }
        }

        private fun loadHistory() {
            viewModelScope.launch {
                getHistoryUseCase().onSuccess { history ->
                    _state.update { it.copy(recentTranslations = history) }
                }
            }
        }
    }
