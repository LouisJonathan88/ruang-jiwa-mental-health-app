package com.example.ruangjiwa.admin.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ruangjiwa.admin.viewmodel.ArtikelViewModel

private val backgroundColor = Color(0xFFF0F4F7)
private val darkTextColor = Color(0xFF2C3E50)
private val primaryColor = Color(0xFF00CED1)
private val cardBackgroundColor = Color.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormTambahEditArtikelScreen(
    artikelId: String? = null,
    viewModel: ArtikelViewModel = viewModel(),
    onSimpanSelesai: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val isEditMode = artikelId != null

    LaunchedEffect(artikelId) {
        if (isEditMode) {
            viewModel.loadArtikel(artikelId!!)
        } else {
            viewModel.resetForm()
        }
    }

    LaunchedEffect(uiState.artikel) {
        uiState.artikel?.let {
            viewModel.setJudul(it.judul)
            viewModel.setIsi(it.isi)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Edit Artikel" else "Tambah Artikel") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor
                )
            )
        }
    ) { padding ->
        Surface(
            color = backgroundColor,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top
            ) {
                OutlinedTextField(
                    value = uiState.judul,
                    onValueChange = { viewModel.setJudul(it) },
                    label = { Text("Judul Artikel", color = darkTextColor) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = cardBackgroundColor,
                        unfocusedContainerColor = cardBackgroundColor,
                        focusedBorderColor = primaryColor,
                        unfocusedBorderColor = primaryColor.copy(alpha = 0.5f)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = uiState.isi,
                    onValueChange = { viewModel.setIsi(it) },
                    label = { Text("Isi Artikel", color = darkTextColor) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = cardBackgroundColor,
                        unfocusedContainerColor = cardBackgroundColor,
                        focusedBorderColor = primaryColor,
                        unfocusedBorderColor = primaryColor.copy(alpha = 0.5f)
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (isEditMode) {
                            viewModel.editArtikel(artikelId!!, onSimpanSelesai)
                        } else {
                            viewModel.tambahArtikel(onSimpanSelesai)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isSaving,
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                ) {
                    if (uiState.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = darkTextColor
                        )
                    } else {
                        Text(
                            text = if (isEditMode) "Update" else "Simpan",
                            color = darkTextColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onSimpanSelesai,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = darkTextColor),
                    border = BorderStroke(1.dp, darkTextColor)
                ) {
                    Text("Batal", color = darkTextColor, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}