package com.polet.panoltli.feature_traductor.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Colores del Maquetado Web original (Modo Oscuro con acentos lilas y morados)
private val BgColor = Color(0xFF0F0F13)
private val CardBg = Color(0xFF181820)
private val AccentPurple = Color(0xFF7C3AED)
private val AccentLilac = Color(0xFFA78BFA)
private val TextMain = Color(0xFFF3F4F6)
private val TextMuted = Color(0xFF9CA3AF)
private val BorderColor = Color(0xFF27272A)

@Composable
fun TranslatorScreen(viewModel: TranslatorViewModel) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(BgColor)
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Título de la Sección
        Text(
            text = "Traductor en Vivo",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = AccentLilac,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        // Visor de Cámara (Simulado / Contenedor)
        Surface(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(240.dp),
            color = CardBg,
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 4.dp,
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (state.isTranslating) {
                    CircularProgressIndicator(color = AccentLilac)
                } else {
                    Text(
                        text = "[ Visor de Cámara Activo ]",
                        color = TextMuted,
                        fontSize = 14.sp,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Panel de Resultado de la Seña Interpretada
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = CardBg,
            shape = RoundedCornerShape(12.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Seña detectada:", fontSize = 12.sp, color = TextMuted)
                Text(
                    text = state.currentSign,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMain,
                    modifier = Modifier.padding(vertical = 4.dp),
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Confianza: ${(state.confidence * 100).toInt()}%",
                    fontSize = 13.sp,
                    color = AccentLilac,
                )

                // Corrección del progress como valor directo y trackColor con Color constante
                LinearProgressIndicator(
                    progress = state.confidence,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                    color = AccentPurple,
                    trackColor = BorderColor,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Historial Reciente de Traducciones en esta sesión
        Text(
            text = "Traducciones Recientes",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextMain,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(state.recentTranslations) { translation ->
                Surface(
                    color = CardBg,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(text = translation.translatedText, color = TextMain, fontSize = 14.sp)
                        Text(
                            text = "${(translation.confidence * 100).toInt()}%",
                            color = AccentLilac,
                            fontSize = 12.sp,
                        )
                    }
                }
            }
        }
    }
}