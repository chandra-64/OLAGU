package com.olagu.ai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.olagu.ai.data.local.LocalChatRepository
import com.olagu.ai.data.model.AiMode
import com.olagu.ai.data.model.AiModel
import com.olagu.ai.data.model.ChatMessage
import com.olagu.ai.data.model.ChatSession
import com.olagu.ai.engine.runtime.OfflineAiEngine
import com.olagu.ai.ui.components.AppSidebar
import com.olagu.ai.ui.components.TopHeader
import com.olagu.ai.ui.theme.PureBlack
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * Main application coordinator screen:
 * - History maintained as full conversation pages (not for every response)
 * - Particular pages have their own scoped history (Documents RAG, Chatbot, Image Creation)
 * - Date sections with a minimal line design between days
 */
@Composable
fun MainScreen() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    var activeMode by remember { mutableStateOf(AiMode.CHATBOT) }
    var selectedModel by remember { mutableStateOf(AiModel.BALANCED) }

    var currentSessionId by remember { mutableStateOf(UUID.randomUUID().toString()) }
    val currentMessages = remember { mutableStateListOf<ChatMessage>() }
    var activeDocumentName by remember { mutableStateOf<String?>(null) }

    var inputText by remember { mutableStateOf("") }
    var isThinking by remember { mutableStateOf(false) }

    fun startFreshPage(mode: AiMode = activeMode, model: AiModel = selectedModel) {
        currentSessionId = UUID.randomUUID().toString()
        currentMessages.clear()
        inputText = ""
        isThinking = false
        activeMode = mode
        selectedModel = model
        activeDocumentName = null
    }

    fun handleSendMessage(prompt: String) {
        if (prompt.isBlank() || isThinking) return

        val userMessage = ChatMessage(
            text = prompt,
            isFromUser = true,
            mode = activeMode,
            model = selectedModel
        )
        currentMessages.add(userMessage)
        inputText = ""

        // Persist the page session with the user's initial message
        val pageTitle = when {
            activeMode == AiMode.DOCUMENT_RAG && activeDocumentName != null -> activeDocumentName!!
            prompt.length > 28 -> prompt.take(26) + "..."
            else -> prompt
        }

        isThinking = true
        coroutineScope.launch {
            val responseText = OfflineAiEngine.generateResponse(
                prompt = prompt,
                mode = activeMode,
                model = selectedModel,
                activeDocumentName = activeDocumentName
            )
            val assistantMessage = ChatMessage(
                text = responseText,
                isFromUser = false,
                mode = activeMode,
                model = selectedModel
            )
            currentMessages.add(assistantMessage)

            // Save the complete page with all responses in history (one page entry)
            val updatedPage = ChatSession(
                id = currentSessionId,
                title = pageTitle,
                mode = activeMode,
                model = selectedModel,
                activeDocumentName = activeDocumentName,
                messages = currentMessages.toList()
            )
            LocalChatRepository.saveOrUpdateSession(updatedPage)

            isThinking = false
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = PureBlack
            ) {
                AppSidebar(
                    activeMode = activeMode,
                    activeSessionId = currentSessionId,
                    historyForCurrentPage = LocalChatRepository.getSessionsForMode(activeMode),
                    onSelectMode = { mode ->
                        if (mode != activeMode) {
                            activeMode = mode
                            // Switch to the latest page of that mode if exists, or start fresh
                            val existingForMode = LocalChatRepository.getSessionsForMode(mode)
                            if (existingForMode.isNotEmpty()) {
                                val latest = existingForMode.first()
                                currentSessionId = latest.id
                                activeMode = latest.mode
                                selectedModel = latest.model
                                activeDocumentName = latest.activeDocumentName
                                currentMessages.clear()
                                currentMessages.addAll(latest.messages)
                                inputText = ""
                            } else {
                                startFreshPage(mode = mode, model = selectedModel)
                            }
                        }
                    },
                    onSelectSession = { session ->
                        currentSessionId = session.id
                        activeMode = session.mode
                        selectedModel = session.model
                        activeDocumentName = session.activeDocumentName
                        currentMessages.clear()
                        currentMessages.addAll(session.messages)
                        inputText = ""
                    },
                    onNewChat = {
                        startFreshPage(mode = activeMode, model = selectedModel)
                    },
                    onDeleteSession = { sessionId ->
                        LocalChatRepository.deleteSession(sessionId)
                        if (currentSessionId == sessionId) {
                            val remaining = LocalChatRepository.getSessionsForMode(activeMode)
                            if (remaining.isNotEmpty()) {
                                val next = remaining.first()
                                currentSessionId = next.id
                                activeMode = next.mode
                                selectedModel = next.model
                                activeDocumentName = next.activeDocumentName
                                currentMessages.clear()
                                currentMessages.addAll(next.messages)
                            } else {
                                startFreshPage(mode = activeMode, model = selectedModel)
                            }
                        }
                    },
                    onCloseSidebar = {
                        coroutineScope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(PureBlack)
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding(),
            containerColor = PureBlack,
            topBar = {
                TopHeader(
                    activeMode = activeMode,
                    selectedModel = selectedModel,
                    onModelSelected = { model ->
                        selectedModel = model
                    },
                    onOpenSidebar = {
                        coroutineScope.launch { drawerState.open() }
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(PureBlack)
            ) {
                SimpleChatView(
                    activeMode = activeMode,
                    messages = currentMessages,
                    activeDocumentName = activeDocumentName,
                    onDocumentSelected = { activeDocumentName = it },
                    inputText = inputText,
                    onInputTextChange = { inputText = it },
                    isThinking = isThinking,
                    onSendMessage = { handleSendMessage(it) }
                )
            }
        }
    }
}
