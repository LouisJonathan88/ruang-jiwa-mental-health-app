package com.example.ruangjiwa.admin.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.ruangjiwa.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import android.content.Intent
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAdminScreen() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val context = LocalContext.current

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "dashboard"

    val backgroundColor = Color(0xFFF0F4F7)
    val darkTextColor = Color(0xFF2C3E50)

    val screenTitle = when (currentRoute) {
        "dashboard" -> "Dashboard"
        "kelola_artikel" -> "Kelola Artikel"
        "formArtikel" -> "Tambah Artikel"
        "formArtikel/{artikelId}" -> "Edit Artikel"
        else -> "Admin Panel"
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                currentRoute = currentRoute,
                onDestinationClicked = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                    scope.launch {
                        drawerState.close()
                    }
                },
                onLogoutClicked = {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    context.startActivity(intent)
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(screenTitle, color = darkTextColor) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = darkTextColor)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = backgroundColor
                    )
                )
            }
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = "dashboard",
                modifier = Modifier.padding(padding)
            ) {
                composable("dashboard") {
                    DashboardAdminScreen()
                }
                composable("kelola_artikel") {
                    ArtikelListScreen(navController = navController)
                }
                composable("formArtikel") {
                    FormTambahEditArtikelScreen(
                        onSimpanSelesai = {
                            navController.popBackStack()
                        }
                    )
                }
                composable(
                    "formArtikel/{artikelId}",
                    arguments = listOf(navArgument("artikelId") { type = NavType.StringType })
                ) { backStackEntry ->
                    FormTambahEditArtikelScreen(
                        artikelId = backStackEntry.arguments?.getString("artikelId"),
                        onSimpanSelesai = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}