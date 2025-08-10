package com.example.ruangjiwa.user.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ruangjiwa.user.model.Article
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AllArticlesViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _allArticles = MutableStateFlow<List<Article>>(emptyList())
    val allArticles: StateFlow<List<Article>> = _allArticles

    init {
        fetchAllArticlesRealtime()
    }

    private fun fetchAllArticlesRealtime() {
        db.collection("artikel")
            .orderBy("tanggal", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    _allArticles.value = emptyList()
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val articlesList = snapshot.documents.mapNotNull { document ->
                        document.toObject(Article::class.java)?.apply {
                            id = document.id
                        }
                    }
                    _allArticles.value = articlesList
                }
            }
    }
}