package com.example.ruangjiwa.user.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import android.app.Application
import com.example.ruangjiwa.user.viewmodel.HomeScreenViewModel
import com.example.ruangjiwa.user.viewmodel.HomeScreenViewModelFactory

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeScreenViewModel = viewModel(factory = HomeScreenViewModelFactory(LocalContext.current.applicationContext as Application))
) {
    val backgroundColor = Color(0xFFF0F4F7)
    val primaryColor = Color(0xFF00CED1)
    val darkTextColor = Color(0xFF2C3E50)
    val softTextColor = Color(0xFF7F8C8D)
    val cardBackgroundColor = Color.White

    val userName by viewModel.userName.collectAsState()
    val dailyQuote by viewModel.dailyQuote.collectAsState()
    val articles by viewModel.articles.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Selamat datang,",
                fontSize = 20.sp,
                color = softTextColor
            )
            Text(
                text = "$userName 👋",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = darkTextColor
            )
            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = cardBackgroundColor
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Quote Harian",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = darkTextColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "\"${dailyQuote.text}\"",
                        color = softTextColor,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "— ${dailyQuote.source}",
                        color = darkTextColor,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Artikel Terbaru",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = darkTextColor
                )
                TextButton(
                    onClick = { navController.navigate("allArticles") }
                ) {
                    Text(
                        text = "Lihat Semua",
                        fontWeight = FontWeight.Medium,
                        color = primaryColor
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(articles) { article ->
                    ArticleCard(
                        article = article,
                        onClick = { articleId ->
                            navController.navigate("articleDetail/$articleId")
                        },
                        darkTextColor = darkTextColor,
                        softTextColor = softTextColor
                    )
                }
            }
        }
    }
}