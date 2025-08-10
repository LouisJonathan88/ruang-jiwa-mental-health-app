package com.example.ruangjiwa.user.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ruangjiwa.user.model.Article
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class ArticleDetailViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _article = MutableStateFlow<Article?>(null)
    val article: StateFlow<Article?> = _article

    fun fetchArticleRealtime(articleId: String) {
        db.collection("artikel").document(articleId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    _article.value = null
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    _article.value = snapshot.toObject(Article::class.java)
                } else {
                    _article.value = null
                }
            }
    }
}