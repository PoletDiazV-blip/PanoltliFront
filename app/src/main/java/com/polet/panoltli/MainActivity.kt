package com.polet.panoltli

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.polet.panoltli.core.data.TokenManager // <-- Importamos TokenManager
import com.polet.panoltli.feature_auth.presentation.AuthScreen
import com.polet.panoltli.feature_auth.presentation.AuthViewModel
import com.polet.panoltli.navigation.MainScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject // <-- Importamos Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Inyectamos TokenManager directamente en la Activity
    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verificamos si ya existe un token guardado al arrancar
        val hasToken = !tokenManager.getToken().isNullOrEmpty()
        val startDestination = if (hasToken) "main" else "auth"

        setContent {
            RootNavHost(startDestination = startDestination)
        }
    }
}

@Composable
fun RootNavHost(startDestination: String) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("auth") {
            val authViewModel = hiltViewModel<AuthViewModel>()
            AuthScreen(
                viewModel = authViewModel,
                onNavigateToDashboard = {
                    navController.navigate("main") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }
        composable("main") {
            MainScreen()
        }
    }
}