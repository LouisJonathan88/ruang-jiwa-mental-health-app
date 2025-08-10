package com.example.ruangjiwa.admin.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ruangjiwa.admin.model.Artikel
import com.example.ruangjiwa.admin.viewmodel.ArtikelListViewModel

private val backgroundColor = Color(0xFFF0F4F7)
private val darkTextColor = Color(0xFF2C3E50)
private val softTextColor = Color(0xFF7F8C8D)
private val primaryColor = Color(0xFF00CED1)
private val cardBackgroundColor = Color.White

private const val MAX_CHARS_PREVIEW = 150

@Composable
fun ArtikelListScreen(navController: NavController) {
    val viewModel: ArtikelListViewModel = viewModel()
    val artikelList by viewModel.artikelList.collectAsState()

    var artikelToDelete by remember { mutableStateOf<Artikel?>(null) }
    var showDeleteAllDialog by remember { mutableStateOf(false) }

    Surface(color = backgroundColor, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            if (artikelList.isNotEmpty()) {
                Button(
                    onClick = { showDeleteAllDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Text("Hapus Semua Artikel")
                }
            }

            if (artikelList.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Belum ada artikel yang dibuat.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = softTextColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(artikelList) { artikel ->
                        ArtikelItem(
                            artikel = artikel,
                            onEdit = {
                                navController.navigate("formArtikel/${artikel.id}")
                            },
                            onDelete = {
                                artikelToDelete = artikel
                            }
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp, end = 16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                onClick = { navController.navigate("formArtikel") },
                containerColor = primaryColor
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Artikel", tint = Color.White)
            }
        }
    }

    if (artikelToDelete != null) {
        AlertDialog(
            onDismissRequest = { artikelToDelete = null },
            title = { Text("Konfirmasi Hapus") },
            text = { Text("Apakah kamu yakin ingin menghapus artikel \"${artikelToDelete?.judul}\"?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        artikelToDelete?.let { viewModel.deleteArtikel(it.id) }
                        artikelToDelete = null
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                TextButton(onClick = { artikelToDelete = null }) {
                    Text("Batal")
                }
            }
        )
    }

    if (showDeleteAllDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAllDialog = false },
            title = { Text("Konfirmasi Hapus Semua") },
            text = { Text("Apakah kamu yakin ingin menghapus semua artikel? Tindakan ini tidak bisa dibatalkan.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteAllArtikel()
                        showDeleteAllDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Hapus Semua")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAllDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}


@Composable
fun ArtikelItem(
    artikel: Artikel,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val isiArtikel = artikel.isi
    val previewText = if (isiArtikel.length > MAX_CHARS_PREVIEW) {
        isiArtikel.take(MAX_CHARS_PREVIEW) + "..."
    } else {
        isiArtikel
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = artikel.judul,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = darkTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = previewText,
                style = MaterialTheme.typography.bodySmall,
                color = softTextColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier.padding(end = 8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = primaryColor),
                    border = BorderStroke(1.dp, primaryColor)
                ) {
                    Text("Edit")
                }
                Button(
                    onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Hapus")
                }
            }
        }
    }
}