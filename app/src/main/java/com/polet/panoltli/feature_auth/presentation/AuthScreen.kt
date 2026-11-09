package com.polet.panoltli.feature_auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.polet.panoltli.BuildConfig
private val BgColor = Color(0xFF0F0F13)
private val CardBg = Color(0xFF181820)
private val AccentPurple = Color(0xFF7C3AED)
private val AccentLilac = Color(0xFFA78BFA)
private val TextMain = Color(0xFFF3F4F6)
private val TextMuted = Color(0xFF9CA3AF)
private val BorderColor = Color(0xFF27272A)

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    onNavigateToDashboard: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var timeLeft by remember { mutableStateOf(60) }
    var canResend by remember { mutableStateOf(false) }

    LaunchedEffect(state.isTwoFactorActive) {
        if (state.isTwoFactorActive) {
            timeLeft = 60
            canResend = false
            while (timeLeft > 0) {
                delay(1000L)
                timeLeft--
            }
            canResend = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 400.dp),
            color = CardBg,
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 4.dp,
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Panoltli",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentLilac,
                )
                Text(
                    text = "Plataforma de Interpretación Inteligente",
                    fontSize = 13.sp,
                    color = TextMuted,
                    modifier = Modifier.padding(bottom = 24.dp),
                )

                when {
                    state.isAuthenticated -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = AccentLilac)
                        }
                    }

                    // PANTALLA Verificación 2FA
                    state.isTwoFactorActive -> {
                        Text(
                            text = "Verificación de 2 Pasos",
                            fontSize = 18.sp,
                            color = TextMain,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 8.dp),
                        )
                        Text(
                            text = "Introduce el código de 6 dígitos enviado a tu correo.",
                            fontSize = 12.sp,
                            color = TextMuted,
                            modifier = Modifier.padding(bottom = 16.dp),
                        )

                        OutlinedTextField(
                            value = state.otpCode,
                            onValueChange = { viewModel.onOtpChanged(it) },
                            label = { Text("Código OTP", color = TextMuted) },
                            singleLine = true,
                            textStyle = TextStyle(color = TextMain, fontSize = 16.sp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextMain,
                                unfocusedTextColor = TextMain,
                                focusedBorderColor = AccentLilac,
                                unfocusedBorderColor = BorderColor,
                                focusedLabelColor = AccentLilac,
                                unfocusedLabelColor = TextMuted
                            ),
                            modifier = Modifier.fillMaxWidth(),
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { viewModel.verify2FA() },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentPurple),
                            enabled = !state.isLoading,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            if (state.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text("Verificar Código", color = Color.White)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        if (canResend) {
                            TextButton(onClick = {
                                timeLeft = 60
                                canResend = false
                                viewModel.resendOtp()
                            }) {
                                Text("Reenviar Código", color = AccentLilac)
                            }
                        } else {
                            Text(
                                text = "Reenviar código en ${timeLeft}s",
                                fontSize = 12.sp,
                                color = TextMuted,
                            )
                        }
                    }

                    // PANTALLAS 1 & 2: Login / Registro
                    else -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(BgColor, RoundedCornerShape(8.dp))
                                .padding(4.dp),
                        ) {
                            TabButton("Iniciar Sesión", state.isLoginTab) {
                                viewModel.onTabChanged(true)
                            }
                            TabButton("Registrarse", !state.isLoginTab) {
                                viewModel.onTabChanged(false)
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        if (!state.isLoginTab) {
                            OutlinedTextField(
                                value = state.name,
                                onValueChange = { viewModel.onNameChanged(it) },
                                label = { Text("Nombre completo", color = TextMuted) },
                                singleLine = true,
                                textStyle = TextStyle(color = TextMain, fontSize = 16.sp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = TextMain,
                                    unfocusedTextColor = TextMain,
                                    focusedBorderColor = AccentLilac,
                                    unfocusedBorderColor = BorderColor,
                                    focusedLabelColor = AccentLilac,
                                    unfocusedLabelColor = TextMuted
                                ),
                                modifier = Modifier.fillMaxWidth(),
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        OutlinedTextField(
                            value = state.email,
                            onValueChange = { viewModel.onEmailChanged(it) },
                            label = { Text("Correo electrónico", color = TextMuted) },
                            singleLine = true,
                            textStyle = TextStyle(color = TextMain, fontSize = 16.sp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextMain,
                                unfocusedTextColor = TextMain,
                                focusedBorderColor = AccentLilac,
                                unfocusedBorderColor = BorderColor,
                                focusedLabelColor = AccentLilac,
                                unfocusedLabelColor = TextMuted
                            ),
                            modifier = Modifier.fillMaxWidth(),
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = state.password,
                            onValueChange = { viewModel.onPasswordChanged(it) },
                            label = { Text("Contraseña", color = TextMuted) },
                            singleLine = true,
                            visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            textStyle = TextStyle(color = TextMain, fontSize = 16.sp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextMain,
                                unfocusedTextColor = TextMain,
                                focusedBorderColor = AccentLilac,
                                unfocusedBorderColor = BorderColor,
                                focusedLabelColor = AccentLilac,
                                unfocusedLabelColor = TextMuted
                            ),
                            trailingIcon = {
                                val image = if (state.isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                                val description = if (state.isPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"

                                IconButton(onClick = { viewModel.togglePasswordVisibility() }) {
                                    Icon(
                                        imageVector = image,
                                        contentDescription = description,
                                        tint = TextMuted
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )

                        if (state.isLoginTab) {
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                                TextButton(onClick = { viewModel.recoverPassword(state.email) }) {
                                    Text("¿Olvidaste tu contraseña?", fontSize = 12.sp, color = TextMuted)
                                }
                            }
                        } else {
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        state.errorMessage?.let { msg ->
                            val isSuccess = msg.contains("éxito", ignoreCase = true) || msg.contains("enviado", ignoreCase = true)
                            Text(
                                text = msg,
                                color = if (isSuccess) Color(0xFF34D399) else Color(0xFFF87171),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(vertical = 8.dp),
                            )
                        }

                        Button(
                            onClick = {
                                if (state.isLoginTab) viewModel.login() else viewModel.register()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentPurple),
                            enabled = !state.isLoading,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            if (state.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    text = if (state.isLoginTab) "Ingresar" else "Registrarse",
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedButton(
                            onClick = {
                                coroutineScope.launch {
                                    try {
                                        val credentialManager = CredentialManager.create(context)

                                        val googleIdOption = GetGoogleIdOption.Builder()
                                            .setFilterByAuthorizedAccounts(false)
                                            .setServerClientId(BuildConfig.GOOGLE_WEB_CLIENT_ID)
                                            .setAutoSelectEnabled(false)
                                            .build()

                                        val request = GetCredentialRequest.Builder()
                                            .addCredentialOption(googleIdOption)
                                            .build()

                                        val result = credentialManager.getCredential(
                                            context = context,
                                            request = request
                                        )
                                        val credential = result.credential

                                        // validar CustomCredential y el tipo correcto de Google
                                        if (credential is androidx.credentials.CustomCredential &&
                                            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                                        ) {
                                            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                                            val googleIdToken = googleIdTokenCredential.idToken
                                            viewModel.signInWithGoogle(googleIdToken)
                                        }
                                    } catch (e: Exception) {
                                        android.util.Log.e("GoogleAuth", "Error al obtener credenciales: ${e.localizedMessage}", e)
                                    }
                                }
                            },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextMain),
                            border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(BorderColor)),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text("Continuar con Google", color = TextMain)
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(state.isAuthenticated) {
        if (state.isAuthenticated) {
            onNavigateToDashboard()
        }
    }
}

@Composable
fun RowScope.TabButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) AccentPurple else Color.Transparent,
        ),
        modifier = Modifier.weight(1f),
    ) {
        Text(text = text, color = if (selected) Color.White else TextMuted, fontSize = 12.sp)
    }
}