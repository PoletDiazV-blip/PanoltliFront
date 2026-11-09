package com.polet.panoltli.feature_diccionario.domain.usecase

import com.polet.panoltli.feature_diccionario.data.repository.DictionaryRepositoryImpl
import com.polet.panoltli.feature_diccionario.domain.model.SignItem
import javax.inject.Inject

class GetSignDictionaryUseCase
    @Inject
    constructor(
        private val repository: DictionaryRepositoryImpl,
    ) {
        suspend operator fun invoke(): Result<List<SignItem>> {
            return repository.getDictionarySigns()
        }
    }
