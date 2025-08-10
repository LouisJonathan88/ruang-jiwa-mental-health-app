package com.example.ruangjiwa.user.model

data class Quote(
    val text: String,
    val source: String
)

val dailyQuotes = listOf(
    Quote(
        text = "Tidak ada kesehatan tanpa kesehatan mental.",
        source = "David Satcher"
    ),
    Quote(
        text = "Kesehatan mentalmu adalah prioritas. Kebahagiaanmu itu penting. Merawat diri sendiri adalah sebuah kebutuhan.",
        source = "Healthy Place"
    ),
    Quote(
        text = "Kamu, lebih dari siapapun di seluruh alam semesta, pantas mendapatkan cinta dan kasih sayangmu sendiri.",
        source = "Buddha"
    ),
    Quote(
        text = "Satu-satunya orang yang bisa membangunmu adalah dirimu sendiri.",
        source = "Oprah Winfrey"
    ),
    Quote(
        text = "Ada harapan, bahkan ketika otakmu berkata tidak ada.",
        source = "John Green"
    ),
    Quote(
        text = "Kesehatan mental sama pentingnya dengan kesehatan fisik.",
        source = "Michelle Obama"
    ),
    Quote(
        text = "Setiap hari mungkin tidak baik, tetapi ada sesuatu yang baik di setiap hari.",
        source = "Alice Morse Earle"
    ),
    Quote(
        text = "Menghadapi depresi secara efektif bukanlah tanda kelemahan, melainkan tanda kekuatan.",
        source = "Andrew Solomon"
    )
)
