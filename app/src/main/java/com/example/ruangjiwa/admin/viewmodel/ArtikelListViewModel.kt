package com.example.ruangjiwa.admin.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ruangjiwa.admin.model.Artikel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class ArtikelListViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _artikelList = MutableStateFlow<List<Artikel>>(emptyList())
    val artikelList: StateFlow<List<Artikel>> = _artikelList.asStateFlow()

    init {
        fetchArtikelListRealtime()
    }

    private fun fetchArtikelListRealtime() {
        db.collection("artikel")
            .orderBy("tanggal", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("ArtikelListViewModel", "Listen failed for articles.", e)
                    _artikelList.value = emptyList()
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull { doc ->
                        try {
                            val id = doc.id
                            val judul = doc.getString("judul") ?: ""
                            val isi = doc.getString("isi") ?: ""
                            val tanggal = doc.getTimestamp("tanggal") ?: Timestamp.now()

                            Artikel(id = id, judul = judul, isi = isi, tanggal = tanggal)
                        } catch (mappingError: Exception) {
                            Log.e("ArtikelListViewModel", "Error mapping document ${doc.id}: ${mappingError.message}", mappingError)
                            null
                        }
                    }
                    _artikelList.value = list
                    Log.d("ArtikelListViewModel", "Articles updated. Total: ${list.size} articles found.")
                } else {
                    Log.d("ArtikelListViewModel", "Snapshot is null, no articles found.")
                    _artikelList.value = emptyList()
                }
            }
    }

    fun deleteArtikel(id: String) {
        viewModelScope.launch {
            try {
                db.collection("artikel").document(id).delete().await()
                Log.d("ArtikelListViewModel", "Artikel dengan ID $id berhasil dihapus.")
            } catch (e: Exception) {
                Log.e("ArtikelListViewModel", "Gagal menghapus artikel: $id", e)
            }
        }
    }

    fun deleteAllArtikel() {
        viewModelScope.launch {
            try {
                val batch = db.batch()
                val snapshot = db.collection("artikel").get().await()

                for (doc in snapshot.documents) {
                    batch.delete(doc.reference)
                }

                batch.commit().await()
                Log.d("ArtikelListViewModel", "Semua artikel berhasil dihapus.")
            } catch (e: Exception) {
                Log.e("ArtikelListViewModel", "Gagal menghapus semua artikel.", e)
            }
        }
    }
}