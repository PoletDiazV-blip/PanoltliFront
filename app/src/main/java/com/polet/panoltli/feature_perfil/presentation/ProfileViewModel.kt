package com.polet.panoltli.feature_perfil.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polet.panoltli.feature_perfil.domain.model.ProfileState
import com.polet.panoltli.feature_perfil.domain.usecase.GetUserProfileUseCase
import com.polet.panoltli.feature_perfil.domain.usecase.UpdatePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
    @Inject
    constructor(
        private val getUserProfileUseCase: GetUserProfileUseCase,
        private val updatePasswordUseCase: UpdatePasswordUseCase,
    ) : ViewModel() {
        private val _state = MutableStateFlow(ProfileState())
        val state: StateFlow<ProfileState> = _state.asStateFlow()

        init {
            loadProfile()
        }

        fun loadProfile() {
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true, errorMessage = null) }
                getUserProfileUseCase().fold(
                    onSuccess = { profile ->
                        _state.update { it.copy(isLoading = false, userProfile = profile) }
                    },
                    onFailure = { error ->
                        _state.update { it.copy(isLoading = false, errorMessage = error.message) }
                    },
                )
            }
        }

        fun changePassword(
            oldPass: String,
            newPass: String,
        ) {
            viewModelScope.launch {
                _state.update { it.copy(isPasswordUpdating = true, errorMessage = null, passwordUpdateSuccess = false) }
                updatePasswordUseCase(oldPass, newPass).fold(
                    onSuccess = {
                        _state.update { it.copy(isPasswordUpdating = false, passwordUpdateSuccess = true) }
                    },
                    onFailure = { error ->
                        _state.update { it.copy(isPasswordUpdating = false, errorMessage = error.message) }
                    },
                )
            }
        }
    }
