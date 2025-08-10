package com.example.ruangjiwa.admin.model


import com.google.firebase.Timestamp // Import Timestamp
import com.google.firebase.firestore.ServerTimestamp // Import ServerTimestamp jika digunakan

data class Artikel(
    val id: String = "",
    val judul: String = "",
    val isi: String = "",
    @ServerTimestamp
    val tanggal: Timestamp = Timestamp.now()
)