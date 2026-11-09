package com.polet.panoltli.navigation

sealed class Screen(val route: String) {
    object Auth : Screen("auth") // <-- Faltaba agregar esta ruta
    object Traductor : Screen("traductor")
    object Diccionario : Screen("diccionario")
    object Historial : Screen("historial")
    object Perfil : Screen("perfil")
}