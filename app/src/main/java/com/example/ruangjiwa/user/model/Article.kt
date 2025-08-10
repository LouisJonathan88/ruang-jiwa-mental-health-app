package com.example.ruangjiwa.user.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Article(
    var id: String = "",
    val judul: String = "",
    val isi: String = "",
    @ServerTimestamp
    val tanggal: Date? = null
)