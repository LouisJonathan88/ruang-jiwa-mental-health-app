package com.example.ruangjiwa.user.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ruangjiwa.user.model.BottomNavItem

@Composable
fun MainScreen(onLogoutSuccess: () -> Unit) {
    val tabNavController = rememberNavController()
    Scaffold(
        bottomBar = {
            MyBottomNavigationBar(navController = tabNavController)
        }
    ) { paddingValues ->
        NavHost(
            navController = tabNavController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen(navController = tabNavController )
            }
            composable(BottomNavItem.Skrining.route) {
                SkriningScreen()
            }
            composable(BottomNavItem.Psikolog.route) {
                PsikologScreen(navController = tabNavController)
            }
            composable(BottomNavItem.Profile.route) {
                ProfileScreen(onLogoutSuccess = onLogoutSuccess)
            }
            composable(
                "articleDetail/{articleId}",
                arguments = listOf(navArgument("articleId") { type = NavType.StringType })
            ) { backStackEntry ->
                ArticleDetailScreen(
                    navController = tabNavController, // Gunakan tabNavController
                    articleId = backStackEntry.arguments?.getString("articleId")
                )
            }
            composable(
                route = "chat_screen/{psikologId}/{namaPsikolog}",
                arguments = listOf(
                    navArgument("psikologId") { type = NavType.StringType },
                    navArgument("namaPsikolog") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                ChatScreen(
                    psikologId = backStackEntry.arguments?.getString("psikologId"),
                    namaPsikolog = backStackEntry.arguments?.getString("namaPsikolog"),
                    navController = tabNavController
                )
            }
            composable("allArticles") {
                AllArticlesScreen(navController = tabNavController)
            }
        }
    }
}