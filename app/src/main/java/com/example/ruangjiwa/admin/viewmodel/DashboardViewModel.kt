package com.example.ruangjiwa.admin.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DashboardViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _totalUserUmum = MutableStateFlow(0)
    val totalUserUmum: StateFlow<Int> = _totalUserUmum.asStateFlow()

    private val _totalPsikolog = MutableStateFlow(0)
    val totalPsikolog: StateFlow<Int> = _totalPsikolog.asStateFlow()

    private val _jumlahArtikel = MutableStateFlow(0)
    val jumlahArtikel: StateFlow<Int> = _jumlahArtikel.asStateFlow()

    private val _statistikGangguan = MutableStateFlow<Map<String, Int>>(emptyMap())
    val statistikGangguan: StateFlow<Map<String, Int>> = _statistikGangguan.asStateFlow()

    init {
        fetchTotalUserUmumRealtime()
        fetchTotalPsikologRealtime()
        fetchJumlahArtikelRealtime()
        fetchStatistikSkriningRealtime()
    }

    private fun fetchTotalUserUmumRealtime() {
        db.collection("users")
            .whereEqualTo("role", "user")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("DashboardViewModel", "Listen failed for total users.", e)
                    _totalUserUmum.value = 0
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    _totalUserUmum.value = snapshot.size()
                }
            }
    }

    private fun fetchTotalPsikologRealtime() {
        db.collection("users")
            .whereEqualTo("role", "psikolog")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("DashboardViewModel", "Listen failed for total psikolog.", e)
                    _totalPsikolog.value = 0
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    _totalPsikolog.value = snapshot.size()
                }
            }
    }

    private fun fetchJumlahArtikelRealtime() {
        db.collection("artikel")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("DashboardViewModel", "Listen failed for total articles.", e)
                    _jumlahArtikel.value = 0
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    _jumlahArtikel.value = snapshot.size()
                }
            }
    }

    private fun fetchStatistikSkriningRealtime() {
        db.collectionGroup("history")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("DashboardViewModel", "Listen failed for screening results.", e)
                    _statistikGangguan.value = emptyMap()
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val aggregatedStats = mutableMapOf<String, Int>()
                    for (doc in snapshot.documents) {
                        try {
                            val category = doc.getString("category")
                            if (category != null) {
                                aggregatedStats[category] = (aggregatedStats[category] ?: 0) + 1
                            }
                        } catch (mappingError: Exception) {
                            Log.e("DashboardViewModel", "Error mapping screening document ${doc.id}: ${mappingError.message}", mappingError)
                        }
                    }
                    _statistikGangguan.value = aggregatedStats
                    Log.d("DashboardViewModel", "Statistik skrining diperbarui: $aggregatedStats")
                } else {
                    _statistikGangguan.value = emptyMap()
                }
            }
    }
}