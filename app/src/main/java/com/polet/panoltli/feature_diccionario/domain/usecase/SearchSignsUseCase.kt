package com.polet.panoltli.feature_diccionario.domain.usecase

import com.polet.panoltli.feature_diccionario.domain.model.SignItem
import javax.inject.Inject

class SearchSignsUseCase
@Inject
constructor() {
    operator fun invoke(
        signs: List<SignItem>,
        query: String,
        category: String,
    ): List<SignItem> {
        return signs.filter { sign ->
            val matchesQuery =
                sign.name.contains(query, ignoreCase = true) ||
                        sign.description.contains(query, ignoreCase = true)
            val matchesCategory = category == "Todos" || sign.category.equals(category, ignoreCase = true)
            matchesQuery && matchesCategory
        }
    }
}