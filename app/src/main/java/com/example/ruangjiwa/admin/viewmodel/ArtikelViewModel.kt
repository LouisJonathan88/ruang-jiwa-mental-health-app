package com.example.ruangjiwa.admin.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ruangjiwa.admin.model.Artikel
import com.example.ruangjiwa.admin.model.ArtikelUiState
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class ArtikelViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _uiState = MutableStateFlow(ArtikelUiState())
    val uiState: StateFlow<ArtikelUiState> = _uiState.asStateFlow()

    fun setJudul(judul: String) {
        _uiState.value = _uiState.value.copy(judul = judul, errorMessage = null)
    }

    fun setIsi(isi: String) {
        _uiState.value = _uiState.value.copy(isi = isi, errorMessage = null)
    }


    fun loadArtikel(artikelId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val doc = db.collection("artikel").document(artikelId).get().await()
                val artikel = doc.toObject(Artikel::class.java)?.copy(id = doc.id)

                if (artikel != null) {
                    _uiState.value = _uiState.value.copy(
                        artikel = artikel,
                        judul = artikel.judul,
                        isi = artikel.isi,
                        tanggal = artikel.tanggal,
                        isLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Artikel tidak ditemukan."
                    )
                }

            } catch (e: Exception) {
                Log.e("ArtikelViewModel", "Gagal memuat artikel: $artikelId", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Terjadi kesalahan saat memuat artikel."
                )
            }
        }
    }

    fun resetForm() {
        _uiState.value = ArtikelUiState()
    }

    fun tambahArtikel(onSelesai: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, errorMessage = null)
            try {
                val artikelBaru = Artikel(
                    judul = _uiState.value.judul,
                    isi = _uiState.value.isi,
                    tanggal = Timestamp.now()
                )
                db.collection("artikel").add(artikelBaru).await()
                _uiState.value = _uiState.value.copy(isSaving = false)
                onSelesai()
            } catch (e: Exception) {
                Log.e("ArtikelViewModel", "Gagal menambahkan artikel", e)
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    errorMessage = "Terjadi kesalahan saat menyimpan artikel baru."
                )
            }
        }
    }

    fun editArtikel(id: String, onSelesai: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, errorMessage = null)
            try {
                val dataToUpdate = mapOf(
                    "judul" to _uiState.value.judul,
                    "isi" to _uiState.value.isi
                )
                db.collection("artikel").document(id).update(dataToUpdate).await()
                _uiState.value = _uiState.value.copy(isSaving = false)
                onSelesai()
            } catch (e: Exception) {
                Log.e("ArtikelViewModel", "Gagal mengupdate artikel: $id", e)
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    errorMessage = "Terjadi kesalahan saat mengupdate artikel."
                )
            }
        }
    }
}