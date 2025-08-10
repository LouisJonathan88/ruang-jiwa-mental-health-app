package com.example.ruangjiwa.user.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import com.example.ruangjiwa.user.model.TestResult

@Composable
fun HistorySkrining() {
    var historyList by remember { mutableStateOf<List<TestResult>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    var showDeleteAllDialog by remember { mutableStateOf(false) }
    var deletingItemId by remember { mutableStateOf<String?>(null) }

    val primaryColor = Color(0xFF00CED1)
    val backgroundColor = Color(0xFFF0F4F7)
    val textColorPrimary = Color(0xFF263238)
    val textColorSecondary = Color(0xFF607D8B)
    val deleteButtonColor = Color(0xFFD32F2F)

    val loadHistory: () -> Unit = {
        isLoading = true
        if (userId != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection("users").document(userId)
                .collection("history")
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val results = querySnapshot.documents.map { document ->
                        TestResult(
                            id = document.id,
                            score = document.getLong("score")?.toInt() ?: 0,
                            category = document.getString("category") ?: "",
                            suggestion = document.getString("suggestion") ?: "",
                            timestamp = document.getLong("timestamp") ?: 0
                        )
                    }
                    historyList = results
                    isLoading = false
                }
                .addOnFailureListener {
                    historyList = emptyList()
                    isLoading = false
                }
        }
    }

    LaunchedEffect(userId) {
        loadHistory()
    }

    fun deleteSingleHistory(documentId: String) {
        if (userId != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection("users").document(userId).collection("history")
                .document(documentId)
                .delete()
                .addOnSuccessListener { loadHistory() }
        }
    }

    fun deleteAllHistory() {
        if (userId != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection("users").document(userId).collection("history")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val batch = db.batch()
                    for (document in querySnapshot.documents) {
                        batch.delete(document.reference)
                    }
                    batch.commit().addOnSuccessListener { historyList = emptyList() }
                }
        }
    }

    if (showDeleteAllDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAllDialog = false },
            title = { Text("Hapus Semua Riwayat?", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) },
            text = { Text("Apakah Anda yakin ingin menghapus semua riwayat tes?.", style = MaterialTheme.typography.bodyMedium) },
            confirmButton = {
                TextButton(
                    onClick = {
                        deleteAllHistory()
                        showDeleteAllDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = deleteButtonColor)
                ) { Text("Ya, Hapus") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAllDialog = false }) { Text("Tidak", color = textColorSecondary) }
            }
        )
    }

    deletingItemId?.let { documentId ->
        AlertDialog(
            onDismissRequest = { deletingItemId = null },
            title = { Text("Hapus Riwayat?", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) },
            text = { Text("Apakah Anda yakin ingin menghapus riwayat ini?", style = MaterialTheme.typography.bodyMedium) },
            confirmButton = {
                TextButton(
                    onClick = {
                        deleteSingleHistory(documentId)
                        deletingItemId = null
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = deleteButtonColor)
                ) { Text("Ya, Hapus") }
            },
            dismissButton = {
                TextButton(onClick = { deletingItemId = null }) { Text("Tidak", color = textColorSecondary) }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Riwayat Skrining",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = textColorPrimary
            )
            if (historyList.isNotEmpty()) {
                TextButton(onClick = { showDeleteAllDialog = true }) {
                    Text("Hapus Semua", color = deleteButtonColor)
                }
            }
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = primaryColor)
            }
        } else if (historyList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Belum ada riwayat tes.", color = textColorSecondary)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(historyList) { result ->
                    HistoryItem(
                        result = result,
                        onDelete = { deletingItemId = result.id },
                        primaryColor = primaryColor,
                        deleteButtonColor = deleteButtonColor
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryItem(
    result: TestResult,
    onDelete: () -> Unit,
    primaryColor: Color,
    deleteButtonColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                val dateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())
                val formattedDate = dateFormat.format(Date(result.timestamp))
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF607D8B)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Skor: ${result.score}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF263238)
                )
                Text(
                    text = "Kategori: ${result.category}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Saran: ${result.suggestion}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF263238)
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Hapus",
                    tint = deleteButtonColor
                )
            }
        }
    }
}