package com.polet.panoltli.feature_diccionario.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polet.panoltli.feature_diccionario.domain.model.DictionaryState
import com.polet.panoltli.feature_diccionario.domain.usecase.GetSignDictionaryUseCase
import com.polet.panoltli.feature_diccionario.domain.usecase.SearchSignsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DictionaryViewModel
    @Inject
    constructor(
        private val getSignDictionaryUseCase: GetSignDictionaryUseCase,
        private val searchSignsUseCase: SearchSignsUseCase,
    ) : ViewModel() {
        private val _state = MutableStateFlow(DictionaryState())
        val state: StateFlow<DictionaryState> = _state.asStateFlow()

        init {
            loadSigns()
        }

        private fun loadSigns() {
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true, errorMessage = null) }
                val result = getSignDictionaryUseCase()
                result.fold(
                    onSuccess = { signs ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                signs = signs,
                                filteredSigns = signs,
                            )
                        }
                    },
                    onFailure = { error ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = error.message ?: "Error al cargar el diccionario",
                            )
                        }
                    },
                )
            }
        }

        // Actualiza la consulta de búsqueda y filtra en tiempo real
        fun onSearchQueryChanged(query: String) {
            _state.update { currentState ->
                val filtered = searchSignsUseCase(currentState.signs, query, currentState.selectedCategory)
                currentState.copy(searchQuery = query, filteredSigns = filtered)
            }
        }

        // Filtra por categoría seleccionada
        fun onCategorySelected(category: String) {
            _state.update { currentState ->
                val filtered = searchSignsUseCase(currentState.signs, currentState.searchQuery, category)
                currentState.copy(selectedCategory = category, filteredSigns = filtered)
            }
        }
    }
