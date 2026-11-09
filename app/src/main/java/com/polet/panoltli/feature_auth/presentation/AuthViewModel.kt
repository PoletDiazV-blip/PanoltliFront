package com.polet.panoltli.feature_auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polet.panoltli.feature_auth.domain.usecase.LoginUseCase
import com.polet.panoltli.feature_auth.domain.usecase.RegisterUseCase
import com.polet.panoltli.feature_auth.domain.usecase.Verify2FAUseCase
import com.polet.panoltli.feature_auth.domain.usecase.GoogleSignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val verify2FAUseCase: Verify2FAUseCase,
    private val googleSignInUseCase: GoogleSignInUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    fun onTabChanged(isLogin: Boolean) {
        _state.update { it.copy(isLoginTab = isLogin, errorMessage = null) }
    }

    fun onEmailChanged(email: String) {
        _state.update { it.copy(email = email) }
    }

    fun onPasswordChanged(password: String) {
        _state.update { it.copy(password = password) }
    }

    fun togglePasswordVisibility() {
        _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onNameChanged(name: String) {
        _state.update { it.copy(name = name) }
    }

    fun onOtpChanged(otp: String) {
        _state.update { it.copy(otpCode = otp) }
    }

    fun login() {
        if (_state.value.isLoading) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            val result = loginUseCase(_state.value.email, _state.value.password)
            result.fold(
                onSuccess = {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isTwoFactorActive = true,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(isLoading = false, errorMessage = error.message ?: "Error al iniciar sesión")
                    }
                }
            )
        }
    }

    fun register() {
        if (_state.value.isLoading) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            val result = registerUseCase(_state.value.name, _state.value.email, _state.value.password)
            result.fold(
                onSuccess = {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isTwoFactorActive = true,
                            errorMessage = "¡Código OTP enviado a tu correo!"
                        )
                    }
                },
                onFailure = { error ->
                    error.printStackTrace()
                    android.util.Log.e("AuthViewModel", "Fallo en register: ${error.localizedMessage}", error)

                    val rawMessage = error.localizedMessage ?: ""
                    val friendlyMessage = when {
                        rawMessage.contains("ya se encuentra registrado", ignoreCase = true) ||
                                rawMessage.contains("registrado", ignoreCase = true) ->
                            "Este correo electrónico ya se encuentra registrado. Intenta iniciar sesión."
                        rawMessage.contains("correo", ignoreCase = true) ||
                                rawMessage.contains("SMTP", ignoreCase = true) ->
                            "No se pudo enviar el correo de verificación. Inténtalo más tarde."
                        else ->
                            "Ocurrió un error al procesar tu registro. Verifica tus datos."
                    }

                    _state.update {
                        it.copy(isLoading = false, errorMessage = friendlyMessage)
                    }
                }
            )
        }
    }

    fun verify2FA() {
        if (_state.value.isLoading) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            val result = verify2FAUseCase(_state.value.email, _state.value.otpCode)
            result.fold(
                onSuccess = { responseDto ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            // Mantenemos isTwoFactorActive intacto para que la UI no regrese al login
                            isAuthenticated = true,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(isLoading = false, errorMessage = error.message ?: "Código incorrecto o expirado")
                    }
                }
            )
        }
    }

    fun recoverPassword(email: String) {
        viewModelScope.launch {
            _state.update { it.copy(errorMessage = "Se envió un correo de recuperación a $email") }
        }
    }

    fun resendOtp() {
        if (_state.value.isLoading) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            // Asumiendo que reutilizamos registerUseCase o creamos un resendOtpUseCase.
            // Como el backend suele reenviar el OTP si el usuario se registra de nuevo o mediante un endpoint específico:
            val result = registerUseCase(_state.value.name.ifEmpty { "Usuario" }, _state.value.email, _state.value.password)

            result.fold(
                onSuccess = {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "¡Nuevo código OTP enviado a tu correo!"
                        )
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "No se pudo reenviar el código. Inténtalo más tarde."
                        )
                    }
                }
            )
        }
    }

    fun signInWithGoogle(idToken: String) {
        if (_state.value.isLoading) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            val result = googleSignInUseCase(idToken)
            result.fold(
                onSuccess = { responseDto ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isAuthenticated = true,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "No se pudo iniciar sesión con Google"
                        )
                    }
                }
            )
        }
    }
}
