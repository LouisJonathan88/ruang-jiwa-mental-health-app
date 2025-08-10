package com.example.ruangjiwa.user.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.ruangjiwa.user.model.Article


@Composable
fun ArticleCard(
    article: Article,
    onClick: (String) -> Unit,
    darkTextColor: Color,
    softTextColor: Color
) {
    Card(
        onClick = { onClick(article.id) },
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = article.judul,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = darkTextColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = article.isi,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = softTextColor
            )
        }
    }
}