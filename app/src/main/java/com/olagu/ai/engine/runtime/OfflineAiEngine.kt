package com.olagu.ai.engine.runtime

import kotlinx.coroutines.delay

/**
 * Offline AI Engine for OLAGU.
 *
 * Runs 100% on-device with zero internet connection required.
 * When you want to connect a full local LLM (such as MediaPipe LLM Inference,
 * llama.cpp / GGUF, or ONNX Runtime), plug your inference code here!
 */
object OfflineAiEngine {

    suspend fun generateResponse(prompt: String): String {
        delay(400)

        val clean = prompt.trim().lowercase()

        return when {
            clean.contains("hello") || clean.contains("hi") || clean.contains("hey") ->
                "Hello! I am OLAGU, your offline AI assistant. How can I help you today?"

            clean.contains("who are you") || clean.contains("what is olagu") || clean.contains("your name") ->
                "I am OLAGU, a 100% offline AI running directly on your phone. Everything stays private on this device."

            clean.contains("how do you work") || clean.contains("offline") ->
                "I process everything on-device without an internet connection. No servers, no tracking, total privacy."

            clean.contains("what can you do") || clean.contains("help") ->
                "I can chat, answer questions, and assist you offline. You can also connect a local LLM model inside engine/runtime/OfflineAiEngine.kt!"

            clean.contains("thank") ->
                "You're very welcome! Let me know if you need anything else."

            clean.contains("bye") || clean.contains("goodbye") ->
                "Goodbye! Have a great day ahead."

            else ->
                "OLAGU received: \"$prompt\"\n\n(Offline mode is active. You can customize this response or plug in your local AI model inside engine/runtime/OfflineAiEngine.kt!)"
        }
    }
}
