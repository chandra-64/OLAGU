package com.olagu.ai.data.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

/**
 * Represents a conversation page in local history.
 * Each entry is a complete page containing all messages and responses for that session.
 */
data class ChatSession(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val mode: AiMode = AiMode.CHATBOT,
    val model: AiModel = AiModel.BALANCED,
    val activeDocumentName: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val timestamp: String = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault()).format(Date()),
    val messages: List<ChatMessage> = emptyList()
)
