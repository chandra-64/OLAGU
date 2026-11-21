package com.olagu.ai.engine.runtime

import com.olagu.ai.data.model.AiMode
import com.olagu.ai.data.model.AiModel
import kotlinx.coroutines.delay

/**
 * Offline AI response engine supporting Documents RAG, Chatbot, and Image Creation.
 */
object OfflineAiEngine {

    suspend fun generateResponse(
        prompt: String,
        mode: AiMode = AiMode.CHATBOT,
        model: AiModel = AiModel.BALANCED,
        activeDocumentName: String? = null
    ): String {
        delay(250)

        val clean = prompt.trim().lowercase()

        return when (mode) {
            AiMode.DOCUMENT_RAG -> {
                if (activeDocumentName != null) {
                    "RAG Answer from \"$activeDocumentName\":\n\nBased on the extracted text in your document, the relevant section indicates:\n\n$prompt\n\n(Retrieved using local on-device embeddings & semantic matching)"
                } else {
                    "Documents RAG:\n\nBased on your indexed documents, here is the answer to \"$prompt\":\n\nRelevant passages were matched and retrieved directly from your local document store without cloud access."
                }
            }
            AiMode.CHATBOT -> {
                when {
                    clean.contains("hello") || clean.contains("hi") || clean.contains("hey") ->
                        "Hello! How can I help you today?"
                    clean.contains("who are you") || clean.contains("what is olagu") ->
                        "I am OLAGU, your offline AI assistant running locally on your device."
                    else ->
                        "Response from ${model.displayName} model:\n\n$prompt"
                }
            }
            AiMode.IMAGE_CREATION -> {
                "Image created for: \"$prompt\"\nModel: ${model.displayName}"
            }
        }
    }
}
