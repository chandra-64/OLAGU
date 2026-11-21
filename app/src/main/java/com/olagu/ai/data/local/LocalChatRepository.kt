package com.olagu.ai.data.local

import androidx.compose.runtime.mutableStateListOf
import com.olagu.ai.data.model.AiMode
import com.olagu.ai.data.model.ChatSession

/**
 * Local chat repository that stores complete conversation pages,
 * maintaining separate history for each particular mode/page rather than for every response.
 */
object LocalChatRepository {

    val history = mutableStateListOf<ChatSession>()

    fun saveOrUpdateSession(session: ChatSession) {
        // Only maintain pages that actually have messages
        if (session.messages.isEmpty()) return

        val index = history.indexOfFirst { it.id == session.id }
        if (index != -1) {
            history[index] = session
        } else {
            history.add(0, session)
        }
    }

    fun getSessionsForMode(mode: AiMode): List<ChatSession> {
        return history.filter { it.mode == mode }
    }

    fun deleteSession(sessionId: String) {
        history.removeAll { it.id == sessionId }
    }

    fun clearAll() {
        history.clear()
    }
}
