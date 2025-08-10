package com.example.ruangjiwa.psikolog.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ruangjiwa.psikolog.model.ChatMessage
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ChatDetailPsikologViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _otherUserName = MutableStateFlow("Pengguna")
    val otherUserName: StateFlow<String> = _otherUserName.asStateFlow()

    private var currentChatId: String? = null
    private var currentOtherUserId: String? = null
    private var psychologistUid: String? = null

    init {
        psychologistUid = auth.currentUser?.uid
        if (psychologistUid == null) {
            Log.e("ChatDetailPsikologVM", "Psychologist UID is null. Cannot initialize chat.")
        }
    }

    fun initializeChat(chatId: String, otherUserId: String) {
        if (chatId == currentChatId && otherUserId == currentOtherUserId) {
            return
        }

        currentChatId = chatId
        currentOtherUserId = otherUserId

        fetchOtherUserName(otherUserId)
        fetchChatMessagesRealtime(chatId)
    }

    private fun fetchOtherUserName(userId: String) {
        viewModelScope.launch {
            try {
                val userDoc = db.collection("users").document(userId).get().await()
                _otherUserName.value = userDoc.getString("name") ?: "Pengguna Tidak Dikenal"
            } catch (e: Exception) {
                Log.e("ChatDetailPsikologVM", "Error fetching other user name: $userId", e)
                _otherUserName.value = "Pengguna Tidak Dikenal"
            }
        }
    }

    private fun fetchChatMessagesRealtime(chatId: String) {
        db.collection("chats").document(chatId).collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("ChatDetailPsikologVM", "Listen failed for chat messages.", e)
                    _messages.value = emptyList()
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val messageList = snapshot.documents.mapNotNull { doc ->
                        try {
                            doc.toObject(ChatMessage::class.java)?.copy(id = doc.id)
                        } catch (mappingError: Exception) {
                            Log.e("ChatDetailPsikologVM", "Error mapping message document ${doc.id}: ${mappingError.message}", mappingError)
                            null
                        }
                    }
                    _messages.value = messageList
                    Log.d("ChatDetailPsikologVM", "Messages updated. Total: ${messageList.size} messages found for chat $chatId.")
                } else {
                    _messages.value = emptyList()
                    Log.d("ChatDetailPsikologVM", "Snapshot is null, no messages found for chat $chatId.")
                }
            }
    }

    fun sendMessage(text: String) {
        if (psychologistUid == null || currentChatId == null || text.isBlank()) {
            Log.w("ChatDetailPsikologVM", "Cannot send message: UID, Chat ID, or text is empty.")
            return
        }

        viewModelScope.launch {
            try {
                val newMessage = ChatMessage(
                    senderId = psychologistUid!!,
                    text = text,
                    timestamp = Timestamp.now()
                )
                db.collection("chats").document(currentChatId!!).collection("messages").add(newMessage).await()

                db.collection("chats").document(currentChatId!!).update(
                    mapOf(
                        "lastMessage" to text,
                        "lastMessageTimestamp" to Timestamp.now(),
                        "lastMessageSenderId" to psychologistUid!!
                    )
                ).await()
                Log.d("ChatDetailPsikologVM", "Message sent and chat updated.")
            } catch (e: Exception) {
                Log.e("ChatDetailPsikologVM", "Failed to send message.", e)
            }
        }
    }
}