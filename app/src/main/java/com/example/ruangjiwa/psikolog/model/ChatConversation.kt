package com.example.ruangjiwa.psikolog.model

import com.google.firebase.Timestamp

data class ChatConversation(
    val id: String = "",
    val otherParticipantId: String = "",
    val otherParticipantName: String = "Pengguna",
    val lastMessage: String = "Belum ada pesan.",
    val lastMessageTimestamp: Timestamp = Timestamp.now(),
    val lastMessageSenderId: String = ""
)

data class ChatMessage(
    val id: String = "",
    val senderId: String = "",
    val text: String = "",
    val timestamp: Timestamp = Timestamp.now()
)

