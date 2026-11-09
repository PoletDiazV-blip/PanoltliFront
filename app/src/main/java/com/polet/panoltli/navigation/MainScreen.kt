package com.polet.panoltli.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.polet.panoltli.feature_diccionario.presentation.DictionaryScreen
import com.polet.panoltli.feature_diccionario.presentation.DictionaryViewModel
import com.polet.panoltli.feature_historial.presentation.HistoryScreen
import com.polet.panoltli.feature_historial.presentation.HistoryViewModel
import com.polet.panoltli.feature_perfil.presentation.ProfileScreen
import com.polet.panoltli.feature_perfil.presentation.ProfileViewModel
import com.polet.panoltli.feature_traductor.presentation.TranslatorScreen
import com.polet.panoltli.feature_traductor.presentation.TranslatorViewModel

private val BgDark = Color(0xFF0F0F13)
private val CardBg = Color(0xFF181820)
private val AccentPurple = Color(0xFF7C3AED)
private val AccentLilac = Color(0xFFA78BFA)
private val TextMuted = Color(0xFF9CA3AF)

sealed class NavigationItem(val route: String, val title: String, val icon: ImageVector) {
    object Traductor : NavigationItem(Screen.Traductor.route, "Traductor", Icons.Default.Home)
    object Diccionario : NavigationItem(Screen.Diccionario.route, "Diccionario", Icons.Default.Search)
    object Historial : NavigationItem(Screen.Historial.route, "Historial", Icons.Default.DateRange)
    object Perfil : NavigationItem(Screen.Perfil.route, "Perfil", Icons.Default.AccountCircle)
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val items = listOf(
        NavigationItem.Traductor,
        NavigationItem.Diccionario,
        NavigationItem.Historial,
        NavigationItem.Perfil,
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Mostramos el BottomBar en las pantallas principales de navegación
    val showBottomBar = currentRoute in items.map { it.route }

    Scaffold(
        containerColor = BgDark,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = CardBg,
                    contentColor = AccentLilac,
                ) {
                    items.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(item.title) },
                            selected = currentRoute == item.route,
                            onClick = {
                                if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = AccentLilac,
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted,
                                indicatorColor = AccentPurple,
                            ),
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Traductor.route, // Arranca directo en el Traductor cuando hay sesión
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            composable(Screen.Traductor.route) {
                val viewModel = hiltViewModel<TranslatorViewModel>()
                TranslatorScreen(viewModel = viewModel)
            }
            composable(Screen.Diccionario.route) {
                val viewModel = hiltViewModel<DictionaryViewModel>()
                DictionaryScreen(viewModel = viewModel)
            }
            composable(Screen.Historial.route) {
                val viewModel = hiltViewModel<HistoryViewModel>()
                HistoryScreen(viewModel = viewModel)
            }
            composable(Screen.Perfil.route) {
                val viewModel = hiltViewModel<ProfileViewModel>()
                ProfileScreen(viewModel = viewModel)
            }
        }
    }
}