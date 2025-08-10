package com.example.ruangjiwa.user.model

data class Question(
    val id: Int,
    val text: String,
    val options: List<String> = listOf("Tidak pernah", "Kadang-kadang", "Sering", "Sangat sering")
)

val testQuestions = listOf(
    Question(id = 1, text = "Saya sering merasa cemas atau khawatir berlebihan terhadap berbagai hal, bahkan tanpa alasan yang jelas."),
    Question(id = 2, text = "Saya merasa sedih, hampa, atau kehilangan semangat hampir sepanjang hari."),
    Question(id = 3, text = "Saya merasa cepat lelah atau kehilangan energi, meskipun tidak melakukan aktivitas berat."),
    Question(id = 4, text = "Saya merasa hidup terasa sangat berat atau tidak ada harapan."),
    Question(id = 5, text = "Saya merasa sulit untuk berkonsentrasi atau membuat keputusan.")
)