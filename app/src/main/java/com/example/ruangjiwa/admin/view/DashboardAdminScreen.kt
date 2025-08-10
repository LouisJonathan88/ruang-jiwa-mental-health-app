package com.example.ruangjiwa.admin.view

import com.example.ruangjiwa.admin.viewmodel.DashboardViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

private val backgroundColor = Color(0xFFF0F4F7)
private val darkTextColor = Color(0xFF2C3E50)
private val softTextColor = Color(0xFF7F8C8D)
private val primaryColor = Color(0xFF00CED1)
private val cardBackgroundColor = Color.White

@Composable
fun DashboardAdminScreen(
    viewModel: DashboardViewModel = viewModel()
) {
    val totalUser by viewModel.totalUserUmum.collectAsState()
    val totalPsikolog by viewModel.totalPsikolog.collectAsState()
    val jumlahArtikel by viewModel.jumlahArtikel.collectAsState()
    val statistikGangguan by viewModel.statistikGangguan.collectAsState()

    Surface(
        color = backgroundColor,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Dashboard Admin",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = darkTextColor,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Ringkasan data dan statistik.",
                style = MaterialTheme.typography.bodyLarge,
                color = softTextColor,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InfoCard(
                    title = "Pengguna Umum",
                    value = "$totalUser",
                    modifier = Modifier.weight(1f)
                )
                InfoCard(
                    title = "Psikolog",
                    value = "$totalPsikolog",
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(12.dp))
            InfoCard(
                title = "Jumlah Artikel",
                value = "$jumlahArtikel"
            )

            Spacer(Modifier.height(32.dp))

            Text(
                text = "Statistik Hasil Skrining",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = darkTextColor
            )
            Spacer(Modifier.height(16.dp))

            if (statistikGangguan.isEmpty()) {
                Text(
                    "Belum ada data hasil skrining.",
                    color = softTextColor
                )
            } else {
                StatistikSkriningCard(statistikGangguan)
            }
        }
    }
}

@Composable
fun InfoCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = darkTextColor
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = primaryColor,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun StatistikSkriningCard(statistik: Map<String, Int>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val total = statistik.values.sum().toFloat()
            statistik.forEach { (kategori, jumlah) ->
                val persentase = if (total > 0) (jumlah * 100 / total).toInt() else 0
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = kategori,
                            style = MaterialTheme.typography.bodyMedium,
                            color = darkTextColor
                        )
                        Text(
                            text = "$jumlah ($persentase%)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = softTextColor
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = if (total > 0) jumlah / total else 0f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        color = primaryColor,
                        trackColor = primaryColor.copy(alpha = 0.2f)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}