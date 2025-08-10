package com.example.ruangjiwa.user.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ruangjiwa.user.viewmodel.PsikologViewModel
import com.example.ruangjiwa.user.model.Psikolog

@Composable
fun PsikologScreen(
    viewModel: PsikologViewModel = viewModel(),
    navController: NavHostController
) {
    val psikologs by viewModel.psikologs.collectAsState()
    val backgroundColor = Color(0xFFF0F4F7)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Text(
            text = "Direktori Psikolog & Psikiater",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Temukan tenaga profesional untuk mendukung kesehatan mental Anda.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        )
        if (psikologs.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(psikologs) { psikolog ->
                    PsikologCard(psikolog = psikolog, navController = navController)
                }
            }
        }
    }
}

@Composable
fun PsikologCard(psikolog: Psikolog, navController: NavHostController) {

    val primaryColor = Color(0xFF00CED1)
    val secondaryTextColor = Color(0xFF5A5A5A)
    val cardBackgroundColor = Color(0xFFF7F7F7)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardBackgroundColor
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = psikolog.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "Spesialisasi: ${psikolog.specialty}",
                style = MaterialTheme.typography.bodyLarge,
                color = primaryColor,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.padding(top = 8.dp))
            Text(
                text = "Lokasi: ${psikolog.location}",
                style = MaterialTheme.typography.bodyMedium,
                color = secondaryTextColor,
            )
            Text(
                text = "Jadwal: ${psikolog.schedule}",
                style = MaterialTheme.typography.bodyMedium,
                color = secondaryTextColor,
            )
            Spacer(modifier = Modifier.padding(top = 16.dp))
            Button(
                onClick = {
                    navController.navigate("chat_screen/${psikolog.id}/${psikolog.name}")
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
            ) {
                Text("Konseling", color = Color.White)
            }
        }
    }
}