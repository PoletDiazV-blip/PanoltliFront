package com.polet.panoltli.feature_historial.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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

@Composable
fun HistoryScreen(viewModel: HistoryViewModel) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(BgColor)
                .padding(16.dp),
    ) {
        // Título de la sección
        Text(
            text = "Historial de Traducciones",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = AccentLilac,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        when {
            state.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AccentLilac)
                }
            }
            state.errorMessage != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = state.errorMessage!!, color = Color(0xFFEF4444), fontSize = 14.sp)
                }
            }
            state.historyItems.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No hay traducciones registradas aún", color = TextMuted, fontSize = 14.sp)
                }
            }
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(state.historyItems) { item ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = CardBg,
                            shape = RoundedCornerShape(12.dp),
                            tonalElevation = 2.dp,
                        ) {
                            Row(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.translatedText,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextMain,
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Confianza: ${(item.confidence * 100).toInt()}% • ${item.timestamp}",
                                        fontSize = 12.sp,
                                        color = TextMuted,
                                    )
                                }

                                IconButton(onClick = { viewModel.deleteItem(item.id) }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Eliminar registro",
                                        tint = Color(0xFFEF4444),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
