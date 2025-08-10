package com.example.ruangjiwa.admin.model

import com.google.firebase.Timestamp

data class ArtikelUiState(
    val artikel: Artikel? = null,
    val judul: String = "",
    val isi: String = "",
    val tanggal: Timestamp = Timestamp.now(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null
)