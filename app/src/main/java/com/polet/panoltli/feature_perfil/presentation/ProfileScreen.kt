package com.polet.panoltli.feature_perfil.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Colores del Maquetado Web original (Modo Oscuro con acentos lilas y morados)
private val BgColor = Color(0xFF0F0F13)
private val CardBg = Color(0xFF181820)
private val AccentPurple = Color(0xFF7C3AED)
private val AccentLilac = Color(0xFFA78BFA)
private val TextMain = Color(0xFFF3F4F6)
private val TextMuted = Color(0xFF9CA3AF)

@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {
    val state by viewModel.state.collectAsState()

    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(BgColor)
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Título de la sección
        Text(
            text = "Mi Perfil",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = AccentLilac,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
        )

        when {
            state.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AccentLilac)
                }
            }
            state.userProfile != null -> {
                val profile = state.userProfile!!

                // Tarjeta de Datos de Usuario
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = CardBg,
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Usuario", fontSize = 12.sp, color = TextMuted)
                        Text(text = profile.username, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextMain)

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(text = "Correo Electrónico", fontSize = 12.sp, color = TextMuted)
                        Text(text = profile.email, fontSize = 16.sp, color = TextMain)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Sección para Cambiar Contraseña
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = CardBg,
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Cambiar Contraseña",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMain,
                            modifier = Modifier.padding(bottom = 12.dp),
                        )

                        OutlinedTextField(
                            value = oldPassword,
                            onValueChange = { oldPassword = it },
                            label = { Text("Contraseña Actual", color = TextMuted) },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            colors =
                                OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AccentPurple,
                                    unfocusedBorderColor = Color(0xFF27272A),
                                    focusedTextColor = TextMain,
                                    unfocusedTextColor = TextMain,
                                ),
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = newPassword,
                            onValueChange = { newPassword = it },
                            label = { Text("Nueva Contraseña", color = TextMuted) },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            colors =
                                OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AccentPurple,
                                    unfocusedBorderColor = Color(0xFF27272A),
                                    focusedTextColor = TextMain,
                                    unfocusedTextColor = TextMain,
                                ),
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                viewModel.changePassword(oldPassword, newPassword)
                                oldPassword = ""
                                newPassword = ""
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = AccentPurple),
                            shape = RoundedCornerShape(8.dp),
                        ) {
                            if (state.isPasswordUpdating) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                            } else {
                                Text(text = "Actualizar Contraseña", color = Color.White)
                            }
                        }

                        if (state.passwordUpdateSuccess) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "¡Contraseña actualizada con éxito!", color = Color(0xFF10B981), fontSize = 13.sp)
                        }

                        if (state.errorMessage != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = state.errorMessage!!, color = Color(0xFFEF4444), fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}
