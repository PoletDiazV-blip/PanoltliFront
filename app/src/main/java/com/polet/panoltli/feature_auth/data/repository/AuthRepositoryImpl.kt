package com.polet.panoltli.feature_auth.data.repository

import com.polet.panoltli.core.data.TokenManager
import com.polet.panoltli.feature_auth.data.dto.GoogleLoginRequestDto
import com.polet.panoltli.feature_auth.data.dto.LoginRequestDto
import com.polet.panoltli.feature_auth.data.dto.LoginResponseDto
import com.polet.panoltli.feature_auth.data.dto.RegisterRequestDto
import com.polet.panoltli.feature_auth.data.dto.VerifyOtpRequestDto
import com.polet.panoltli.feature_auth.data.remote.AuthApi
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun register(name: String, email: String, password: String): Result<String> {
        return try {
            val response = authApi.register(RegisterRequestDto(name, email, password))
            if (response.isSuccessful) {
                Result.success("¡Código OTP enviado a tu correo!")
            } else {
                val errorBody = response.errorBody()?.string() ?: "Cuerpo de error vacío"
                android.util.Log.e("PanoltliDebug", "ERROR HTTP ${response.code()}: $errorBody")
                Result.failure(Exception("Error HTTP ${response.code()}: $errorBody"))
            }
        } catch (e: Exception) {
            android.util.Log.e("PanoltliDebug", "EXCEPCIÓN DE RED: ${e.localizedMessage}", e)
            Result.failure(e)
        }
    }

    override suspend fun verifyOtp(email: String, otp: String): Result<LoginResponseDto> {
        return try {
            val response = authApi.verifyOtp(VerifyOtpRequestDto(email, otp))
            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!

                // 2. Guardamos el token localmente de inmediato
                tokenManager.saveToken(loginResponse.token)

                Result.success(loginResponse)
            } else {
                Result.failure(Exception("Código OTP inválido o expirado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(email: String, password: String): Result<LoginResponseDto> {
        return try {
            val response = authApi.login(LoginRequestDto(email, password))
            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!

                // Opcional pero recomendado: guardar también en login convencional
                tokenManager.saveToken(loginResponse.token)

                Result.success(loginResponse)
            } else {
                Result.failure(Exception("Credenciales inválidas o cuenta sin verificar"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun googleSignIn(idToken: String): Result<LoginResponseDto> {
        return try {
            val request = GoogleLoginRequestDto(idToken = idToken)
            val response = authApi.googleSignIn(request)

            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!

                // 3. Guardamos el token de Google localmente
                tokenManager.saveToken(loginResponse.token)

                Result.success(loginResponse)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Error en la autenticación con Google"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}