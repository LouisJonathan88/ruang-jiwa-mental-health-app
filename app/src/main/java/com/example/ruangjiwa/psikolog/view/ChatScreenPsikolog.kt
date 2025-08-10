package com.example.ruangjiwa.psikolog.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ruangjiwa.psikolog.model.ChatConversation
import com.example.ruangjiwa.psikolog.viewmodel.ChatListPsikologViewModel
import java.text.SimpleDateFormat
import java.util.*

private val backgroundColor = Color(0xFFF0F4F7)
private val darkTextColor = Color(0xFF2C3E50)
private val softTextColor = Color(0xFF7F8C8D)
private val primaryColor = Color(0xFF00CED1)
private val cardBackgroundColor = Color.White

@Composable
fun ChatScreenPsikolog(
    navController: NavController,
    viewModel: ChatListPsikologViewModel = viewModel()
) {
    val chatConversations by viewModel.chatConversations.collectAsState()

    Surface(
        color = backgroundColor,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            if (chatConversations.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Belum ada percakapan chat.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = softTextColor,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(chatConversations) { conversation ->
                        ChatConversationItem(
                            conversation = conversation,
                            onClick = { chatId, userId ->
                                navController.navigate("chat_detail_psikolog/$chatId/$userId")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatConversationItem(
    conversation: ChatConversation,
    onClick: (chatId: String, userId: String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(conversation.id, conversation.otherParticipantId) },
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = conversation.otherParticipantName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = darkTextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(conversation.lastMessageTimestamp.toDate()),
                    style = MaterialTheme.typography.bodySmall,
                    color = softTextColor
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = conversation.lastMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = softTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}