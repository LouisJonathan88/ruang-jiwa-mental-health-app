package com.example.ruangjiwa.user.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Locale
import com.example.ruangjiwa.user.model.Message

@Composable
fun ChatBubble(message: Message) {
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    val isSentByMe = message.senderId == currentUserId

    val primaryColor = Color(0xFF00CED1)
    val bubbleColor = if (isSentByMe) primaryColor else Color.White
    val textColor = if (isSentByMe) Color.White else Color.Black

    val boxAlignment = if (isSentByMe) Alignment.CenterEnd else Alignment.CenterStart
    val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(message.timestamp.toDate())

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = boxAlignment
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(bubbleColor)
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .widthIn(min = 48.dp, max = 250.dp)
        ) {
            Column {
                Text(
                    text = message.text,
                    color = textColor,
                    modifier = Modifier.widthIn(max = 226.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = time,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSentByMe) Color.White.copy(alpha = 0.7f) else Color.Gray,
                    modifier = Modifier.align(Alignment.End),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}