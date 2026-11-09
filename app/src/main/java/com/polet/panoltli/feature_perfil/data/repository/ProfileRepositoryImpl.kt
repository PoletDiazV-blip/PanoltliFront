package com.polet.panoltli.feature_perfil.data.repository

import com.polet.panoltli.feature_perfil.data.remote.ProfileApi
import com.polet.panoltli.feature_perfil.data.remote.UpdatePasswordRequestDto
import com.polet.panoltli.feature_perfil.domain.model.UserProfile
import javax.inject.Inject

class ProfileRepositoryImpl
    @Inject
    constructor(
        private val api: ProfileApi,
    ) {
        suspend fun getUserProfile(): Result<UserProfile> {
            return try {
                val response = api.getUserProfile()
                if (response.isSuccessful && response.body() != null) {
                    val dto = response.body()!!
                    Result.success(
                        UserProfile(
                            id = dto.id,
                            username = dto.username,
                            email = dto.email,
                            profilePictureUrl = dto.profilePictureUrl,
                        ),
                    )
                } else {
                    Result.failure(Exception("Error al obtener el perfil de usuario"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

        suspend fun updatePassword(
            oldPass: String,
            newPass: String,
        ): Result<Unit> {
            return try {
                val response = api.updatePassword(UpdatePasswordRequestDto(oldPass, newPass))
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("La contraseña actual es incorrecta o falló el cambio"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
