package com.olagu.ai.data.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

enum class MessageType {
    TEXT,
    DATA_RESULT,
    IMAGE_RESULT
}

/**
 * Represents an individual chat message in the conversation.
 */
data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val isFromUser: Boolean,
    val mode: AiMode = AiMode.CHATBOT,
    val model: AiModel = AiModel.BALANCED,
    val type: MessageType = MessageType.TEXT,
    val imageUrl: String? = null,
    val timestamp: String = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
)
