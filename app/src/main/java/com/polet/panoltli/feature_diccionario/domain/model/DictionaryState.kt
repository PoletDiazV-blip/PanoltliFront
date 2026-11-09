package com.polet.panoltli.feature_diccionario.domain.model

data class DictionaryState(
    val signs: List<SignItem> = emptyList(),
    val filteredSigns: List<SignItem> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: String = "Todos",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)