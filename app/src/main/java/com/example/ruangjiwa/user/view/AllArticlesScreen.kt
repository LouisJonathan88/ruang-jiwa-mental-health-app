package com.example.ruangjiwa.user.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ruangjiwa.user.viewmodel.AllArticlesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllArticlesScreen(
    navController: NavController,
    viewModel: AllArticlesViewModel = viewModel()
) {
    val backgroundColor = Color(0xFFF0F4F7)
    val darkTextColor = Color(0xFF2C3E50)
    val softTextColor = Color(0xFF7F8C8D)
    val allArticles by viewModel.allArticles.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Semua Artikel") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor
                )
            )
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(allArticles) { article ->
                ArticleCard(article = article, onClick = { articleId ->
                    navController.navigate("articleDetail/$articleId")
                },
                    darkTextColor = darkTextColor,
                    softTextColor = softTextColor
                )
            }
        }
    }
}