package com.example.ruangjiwa.psikolog.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ruangjiwa.psikolog.model.ChatConversation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.Query

class ChatListPsikologViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _chatConversations = MutableStateFlow<List<ChatConversation>>(emptyList())
    val chatConversations: StateFlow<List<ChatConversation>> = _chatConversations.asStateFlow()

    private var psychologistUid: String? = null

    init {
        psychologistUid = auth.currentUser?.uid
        if (psychologistUid != null) {
            fetchChatConversationsRealtime(psychologistUid!!)
        } else {
            Log.e("ChatListPsikologVM", "Psychologist UID is null. Cannot fetch chats.")
        }
    }

    private fun fetchChatConversationsRealtime(psychologistId: String) {
        db.collection("chats")
            .whereArrayContains("participants", psychologistId)
            .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("ChatListPsikologVM", "Listen failed for chat conversations.", e)
                    _chatConversations.value = emptyList()
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    viewModelScope.launch {
                        val conversations = mutableListOf<ChatConversation>()
                        for (doc in snapshot.documents) {
                            try {
                                val participants = doc.get("participants") as? List<String>
                                val lastMessage = doc.getString("lastMessage") ?: "Belum ada pesan."
                                val lastMessageTimestamp = doc.getTimestamp("lastMessageTimestamp")
                                val lastMessageSenderId = doc.getString("lastMessageSenderId") ?: ""

                                if (participants != null && participants.contains(psychologistId)) {
                                    val otherParticipantId = participants.firstOrNull { it != psychologistId }

                                    var otherParticipantName = "Pengguna Tidak Dikenal"
                                    if (otherParticipantId != null) {
                                        val userDoc = db.collection("users").document(otherParticipantId).get().await()
                                        otherParticipantName = userDoc.getString("name") ?: "Pengguna Tidak Dikenal"
                                    }

                                    conversations.add(
                                        ChatConversation(
                                            id = doc.id,
                                            otherParticipantId = otherParticipantId ?: "",
                                            otherParticipantName = otherParticipantName,
                                            lastMessage = lastMessage,
                                            lastMessageTimestamp = lastMessageTimestamp ?: com.google.firebase.Timestamp.now(),
                                            lastMessageSenderId = lastMessageSenderId
                                        )
                                    )
                                }
                            } catch (mappingError: Exception) {
                                Log.e("ChatListPsikologVM", "Error mapping chat document ${doc.id}: ${mappingError.message}", mappingError)
                            }
                        }
                        _chatConversations.value = conversations
                        Log.d("ChatListPsikologVM", "Chat conversations updated. Total: ${conversations.size}")
                    }
                } else {
                    _chatConversations.value = emptyList()
                    Log.d("ChatListPsikologVM", "Snapshot is null, no chat conversations found.")
                }
            }
    }
}