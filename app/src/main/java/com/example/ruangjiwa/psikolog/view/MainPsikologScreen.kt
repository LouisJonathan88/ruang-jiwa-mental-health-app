package com.example.ruangjiwa.psikolog.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ruangjiwa.psikolog.model.BottomNavItemPsikolog

private val backgroundColor = Color(0xFFF0F4F7)
private val darkTextColor = Color(0xFF2C3E50)
private val primaryColor = Color(0xFF00CED1)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPsikologScreen(
    mainNavController: NavController
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            if (currentRoute != "chat_detail_psikolog/{chatId}/{userId}") {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = when (currentRoute) {
                                BottomNavItemPsikolog.Chat.route -> "Chat dengan Pengguna"
                                BottomNavItemPsikolog.Profile.route -> "Profil Saya"
                                else -> "Psikolog Panel"
                            },
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = darkTextColor,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = backgroundColor
                    )
                )
            }
        },
        bottomBar = {
            if (currentRoute != "chat_detail_psikolog/{chatId}/{userId}") {
                MyBottomNavigationBarPsikolog(navController = navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItemPsikolog.Chat.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(BottomNavItemPsikolog.Chat.route) {
                ChatScreenPsikolog(navController = navController)
            }
            composable(BottomNavItemPsikolog.Profile.route) {
                ProfilePsikologScreen(
                    onLogoutSuccess = {
                        mainNavController.navigate("login") {
                            popUpTo(mainNavController.graph.id) { inclusive = true }
                        }
                    }
                )
            }
            composable(
                "chat_detail_psikolog/{chatId}/{userId}",
                arguments = listOf(
                    navArgument("chatId") { type = NavType.StringType },
                    navArgument("userId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
                val userId = backStackEntry.arguments?.getString("userId") ?: ""
                ChatDetailScreenPsikolog(navController = navController, chatId = chatId, userId = userId)
            }
        }
    }
}