package com.polet.panoltli.feature_diccionario.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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

@Composable
fun DictionaryScreen(viewModel: DictionaryViewModel) {
    val state by viewModel.state.collectAsState()
    val categories = listOf("Todos", "Abecedario", "Saludos", "Preguntas", "Verbos")

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(BgColor)
                .padding(16.dp),
    ) {
        // Título de la sección
        Text(
            text = "Diccionario de Señas",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = AccentLilac,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        // Barra de Búsqueda (usando searchQuery del State y onSearchQueryChanged del ViewModel)
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = { viewModel.onSearchQueryChanged(it) },
            label = { Text("Buscar seña o descripción...", color = TextMuted) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors =
                OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentPurple,
                    unfocusedBorderColor = Color(0xFF27272A),
                    focusedTextColor = TextMain,
                    unfocusedTextColor = TextMain,
                ),
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Filtros por Categoría con Surface (100% compatible y sin errores de versión)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 16.dp),
        ) {
            items(categories) { category ->
                val isSelected = state.selectedCategory == category
                Surface(
                    onClick = { viewModel.onCategorySelected(category) },
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) AccentPurple else CardBg,
                    modifier = Modifier.height(32.dp)
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = category,
                            fontSize = 12.sp,
                            color = if (isSelected) Color.White else TextMuted
                        )
                    }
                }
            }
        }

        // Contenido Principal (Cargando, Errores o Lista Filtrada)
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
            state.filteredSigns.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No se encontraron señas", color = TextMuted, fontSize = 14.sp)
                }
            }
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(state.filteredSigns) { sign ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = CardBg,
                            shape = RoundedCornerShape(12.dp),
                            tonalElevation = 2.dp,
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text = sign.name,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextMain,
                                    )
                                    Surface(
                                        color = AccentPurple.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(6.dp),
                                    ) {
                                        Text(
                                            text = sign.category,
                                            fontSize = 11.sp,
                                            color = AccentLilac,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = sign.description,
                                    fontSize = 13.sp,
                                    color = TextMuted,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}