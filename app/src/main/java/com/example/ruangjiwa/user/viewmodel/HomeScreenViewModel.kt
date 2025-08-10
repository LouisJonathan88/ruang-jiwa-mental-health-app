package com.example.ruangjiwa.user.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.ruangjiwa.user.model.Article
import com.example.ruangjiwa.user.model.Quote
import com.example.ruangjiwa.user.model.dailyQuotes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeScreenViewModel (application: Application) : AndroidViewModel(application) {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val prefs = application.getSharedPreferences("app_prefs", Application.MODE_PRIVATE)

    private val _userName = MutableStateFlow("User")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _dailyQuote = MutableStateFlow(Quote("", ""))
    val dailyQuote: StateFlow<Quote> = _dailyQuote.asStateFlow()

    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles: StateFlow<List<Article>> = _articles.asStateFlow()


    init {
        fetchUserData()
        fetchDailyQuote()
        fetchArticlesRealtime()
    }

    private fun fetchUserData() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            db.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    val name = document.getString("name")
                    if (!name.isNullOrEmpty()) {
                        _userName.value = name
                    }
                }
        }
    }

    private fun fetchDailyQuote() {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val lastQuoteDate = prefs.getString("last_quote_date", "")
        val savedQuoteText = prefs.getString("daily_quote_text", "")
        val savedQuoteSource = prefs.getString("daily_quote_source", "")

        if (today == lastQuoteDate && savedQuoteText != null && savedQuoteSource != null) {
            _dailyQuote.value = Quote(savedQuoteText, savedQuoteSource)
        } else {
            val newQuote = dailyQuotes.random()
            _dailyQuote.value = newQuote
            prefs.edit().apply {
                putString("last_quote_date", today)
                putString("daily_quote_text", newQuote.text)
                putString("daily_quote_source", newQuote.source)
                apply()
            }
        }
    }

    private fun fetchArticlesRealtime() {
        db.collection("artikel")
            .orderBy("tanggal", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(3)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    _articles.value = emptyList()
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val articlesList = snapshot.documents.mapNotNull { document ->
                        document.toObject(Article::class.java)?.apply {
                            id = document.id
                        }
                    }
                    _articles.value = articlesList
                }
            }
    }
}