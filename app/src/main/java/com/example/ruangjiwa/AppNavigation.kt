package com.example.ruangjiwa

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ruangjiwa.admin.view.MainAdminScreen
import com.example.ruangjiwa.auth.view.LoginScreen
import com.example.ruangjiwa.auth.view.RegisterScreen
import com.example.ruangjiwa.auth.viewmodel.AuthViewModel
import com.example.ruangjiwa.psikolog.view.MainPsikologScreen
import com.example.ruangjiwa.user.view.MainScreen
import kotlinx.coroutines.delay

@Composable
fun AppNavigation() {
    var showSplash by remember { mutableStateOf(true) }
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    LaunchedEffect(Unit) {
        delay(3000)
        showSplash = false
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (showSplash) "splash" else "login"
    ) {
        composable("splash") {
            SplashScreen()
        }
        composable("login") {
            LoginScreen(navController = navController, viewModel = authViewModel)
        }
        composable("register") {
            RegisterScreen(navController = navController)
        }
        composable("home") {
            MainScreen(onLogoutSuccess = {
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
            })
        }
        composable("admin_dashboard") {
            MainAdminScreen()
        }
        composable("psikolog") {
            MainPsikologScreen(mainNavController = navController)
        }
    }
}
