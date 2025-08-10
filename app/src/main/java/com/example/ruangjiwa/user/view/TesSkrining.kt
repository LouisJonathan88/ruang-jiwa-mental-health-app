package com.example.ruangjiwa.user.view

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.example.ruangjiwa.user.model.testQuestions
import com.example.ruangjiwa.user.model.Result

@Composable
fun TestSkrining() {
    var isTestStarted by remember { mutableStateOf(false) }
    var userAnswers by remember { mutableStateOf(mapOf<Int, Int>()) }
    var result by remember { mutableStateOf<Result?>(null) }

    val primaryColor = Color(0xFF00CED1)
    val backgroundColor = Color(0xFFF0F4F7)
    val cardBackgroundColor = Color.White
    val textColorPrimary = Color(0xFF263238)
    val textColorSecondary = Color(0xFF607D8B)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (!isTestStarted && result == null) {
            InstructionsSection(primaryColor = primaryColor, onStartTest = { isTestStarted = true })
        } else if (isTestStarted && result == null) {

            QuestionsSection(
                userAnswers = userAnswers,
                onAnswerSelected = { questionId, answerId ->
                    userAnswers = userAnswers.toMutableMap().apply {
                        this[questionId] = answerId
                    }
                },
                onFinishTest = {
                    val score = userAnswers.values.sum()
                    val (category, suggestion) = getCategoryAndSuggestion(score)
                    val newResult = Result(score, category, suggestion, System.currentTimeMillis())
                    result = newResult
                    saveTestResult(newResult)
                    isTestStarted = false
                },
                primaryColor = primaryColor
            )
        } else if (result != null) {
            ResultScreen(
                result = result!!,
                onBackToTest = {
                    result = null
                    userAnswers = mapOf()
                }
            )
        }
    }
}

@Composable
fun InstructionsSection(primaryColor: Color, onStartTest: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Skrining Kesehatan Mental",
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold,
            color = primaryColor
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Kenali kondisi emosional Anda dalam 2 minggu terakhir.",
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,
            color = Color(0xFF607D8B)
        )
        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Petunjuk:",
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF263238)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Pilih jawaban yang paling sesuai dengan kondisi Anda. Tes ini bukan diagnosis, melainkan indikasi awal.",
                    style = MaterialTheme.typography.body2,
                    color = Color(0xFF263238)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Skala Jawaban:",
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF263238)
                )
                Spacer(modifier = Modifier.height(8.dp))
                listOf(
                    "Tidak pernah (0)",
                    "Kadang-kadang (1)",
                    "Sering (2)",
                    "Sangat sering (3)"
                ).forEach {
                    Text(text = "• $it", style = MaterialTheme.typography.body2, color = Color(0xFF263238))
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onStartTest,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
        ) {
            Text("Mulai Tes", color = Color.White, modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionsSection(
    userAnswers: Map<Int, Int>,
    onAnswerSelected: (Int, Int) -> Unit,
    onFinishTest: () -> Unit,
    primaryColor: Color
) {
    val isAllAnswered = userAnswers.size == testQuestions.size

    testQuestions.forEach { question ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = question.text,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.subtitle1,
                    color = Color(0xFF263238)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Column(Modifier.selectableGroup()) {
                    question.options.forEachIndexed { index, option ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = (userAnswers[question.id] == index),
                                    onClick = { onAnswerSelected(question.id, index) }
                                )
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (userAnswers[question.id] == index),
                                onClick = { onAnswerSelected(question.id, index) },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = primaryColor,
                                    unselectedColor = Color(0xFFB0BEC5)
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = option, modifier = Modifier.padding(start = 8.dp), color = Color(0xFF263238))
                        }
                    }
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(24.dp))

    Button(
        onClick = onFinishTest,
        enabled = isAllAnswered,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = primaryColor,
            disabledContainerColor = Color(0xFFB0BEC5)
        )
    ) {
        Text("Selesai", color = Color.White, modifier = Modifier.padding(vertical = 4.dp))
    }
}

fun saveTestResult(result: Result) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    Log.d("SkriningScreen", "User ID: $userId")
    if (userId != null) {
        val db = FirebaseFirestore.getInstance()
        val historyRef = db.collection("users").document(userId).collection("history")

        val resultData = hashMapOf(
            "score" to result.score,
            "category" to result.category,
            "suggestion" to result.suggestion,
            "timestamp" to result.timestamp
        )
        historyRef.add(resultData)
            .addOnSuccessListener {
                Log.d("SkriningScreen", "Hasil tes berhasil disimpan!")
            }
            .addOnFailureListener { e ->
                Log.e("SkriningScreen", "Gagal menyimpan hasil tes: ${e.message}")
            }
    } else {
        Log.e("SkriningScreen", "Tidak ada pengguna yang login, data tidak disimpan.")
    }
}

fun getCategoryAndSuggestion(score: Int): Pair<String, String> {
    return when {
        score >= 13 -> "Berat" to "Menunjukkan indikasi kuat gangguan kecemasan atau depresi. Disarankan segera mencari bantuan profesional seperti psikolog atau psikiater."
        score >= 10 -> "Sedang" to "Kemungkinan mengalami gejala kecemasan atau depresi. Disarankan melakukan konsultasi awal dengan tenaga profesional atau konselor."
        score >= 5 -> "Ringan" to "Perhatikan kondisi psikologis. Disarankan mengelola stres, menjaga rutinitas sehat, dan melakukan pemantauan mandiri secara berkala."
        else -> "Tidak ada atau sangat ringan" to "Tidak menunjukkan gejala signifikan. Tidak memerlukan tindak lanjut khusus."
    }
}