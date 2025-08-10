package com.example.ruangjiwa.user.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ruangjiwa.user.model.Psikolog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PsikologViewModel : ViewModel() {
    private val _psikologs = MutableStateFlow<List<Psikolog>>(emptyList())
    val psikologs: StateFlow<List<Psikolog>> = _psikologs
    init {
        fetchPsikologs()
    }
    private fun fetchPsikologs() {
        viewModelScope.launch {
            try {
                val db = FirebaseFirestore.getInstance()
                val querySnapshot = db.collection("users")
                    .whereEqualTo("role", "psikolog")
                    .get()
                    .await()
                val psikologList = querySnapshot.documents.mapNotNull { document ->
                    document.toObject(Psikolog::class.java)?.copy(id = document.id)
                }
                _psikologs.value = psikologList
                Log.d("PsikologViewModel", "Psikolog berhasil dimuat: ${psikologList.size} orang")
            } catch (e: Exception) {
                Log.e("PsikologViewModel", "Gagal memuat psikolog", e)
            }
        }
    }
}