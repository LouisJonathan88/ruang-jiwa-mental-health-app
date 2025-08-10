package com.example.ruangjiwa.user.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.example.ruangjiwa.user.model.Message

class ChatViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private var currentChatId: String? = null
    private var currentPsikologId: String? = null

    fun initializeChat(psikologId: String) {
        val currentUser = auth.currentUser ?: return
        val senderId = currentUser.uid
        val chatId = getChatId(senderId, psikologId)

        if (chatId == currentChatId && psikologId == currentPsikologId) {
            return
        }
        currentChatId = chatId
        currentPsikologId = psikologId

        getChatMessages(psikologId)
    }

    fun sendMessage(psikologId: String, text: String) {
        val currentUser = auth.currentUser ?: return
        val senderId = currentUser.uid
        val chatId = getChatId(senderId, psikologId)

        viewModelScope.launch {
            try {
                val chatDocRef = db.collection("chats").document(chatId)

                val message = Message(
                    senderId = senderId,
                    text = text,
                    timestamp = Timestamp.now()
                )
                chatDocRef.collection("messages").add(message).await()
                Log.d("ChatViewModel", "Message added to subcollection: $chatId")

                val updateData = mapOf(
                    "lastMessage" to text,
                    "lastMessageTimestamp" to Timestamp.now(),
                    "lastMessageSenderId" to senderId
                )

                val chatDocSnapshot = chatDocRef.get().await()
                if (!chatDocSnapshot.exists()) {
                    val initialChatData = mapOf(
                        "participants" to listOf(senderId, psikologId),
                        "lastMessage" to text,
                        "lastMessageTimestamp" to Timestamp.now(),
                        "lastMessageSenderId" to senderId
                    )
                    chatDocRef.set(initialChatData).await()
                    Log.d("ChatViewModel", "New chat document created: $chatId")
                } else {
                    chatDocRef.update(updateData).await()
                    Log.d("ChatViewModel", "Existing chat document updated: $chatId")
                }

            } catch (e: Exception) {
                Log.e("ChatViewModel", "Failed to send message or update chat: ${e.message}", e)
            }
        }
    }

    fun getChatMessages(psikologId: String) {
        val currentUser = auth.currentUser ?: return
        val senderId = currentUser.uid
        val chatId = getChatId(senderId, psikologId)

        db.collection("chats").document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("ChatViewModel", "Listen failed for chat messages: ${e.message}", e)
                    _messages.value = emptyList()
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val messages = snapshot.documents.mapNotNull { doc ->
                        try {
                            doc.toObject(Message::class.java)?.copy(id = doc.id)
                        } catch (mappingError: Exception) {
                            Log.e("ChatViewModel", "Error mapping message document ${doc.id}: ${mappingError.message}", mappingError)
                            null
                        }
                    }
                    _messages.value = messages
                    Log.d("ChatViewModel", "Messages updated for chat $chatId. Total: ${messages.size}")
                } else {
                    Log.d("ChatViewModel", "Snapshot is null, no messages found for chat $chatId.")
                }
            }
    }
    private fun getChatId(user1: String, user2: String): String {
        return if (user1 < user2) {
            "${user1}_${user2}"
        } else {
            "${user2}_${user1}"
        }
    }
}