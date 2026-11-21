package com.olagu.ai.ui.screens

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.olagu.ai.data.model.AiMode
import com.olagu.ai.data.model.ChatMessage
import com.olagu.ai.ui.components.LuxuryIcons
import com.olagu.ai.ui.theme.PureBlack
import com.olagu.ai.ui.theme.PureWhite
import com.olagu.ai.ui.theme.SurfaceCard
import com.olagu.ai.ui.theme.SurfaceSelected
import com.olagu.ai.ui.theme.TextMuted
import com.olagu.ai.ui.theme.TextPrimary
import com.olagu.ai.ui.theme.TextSecondary

/**
 * Clean & borderless chat interface supporting Documents RAG, Chatbot, and Image Creation.
 */
@Composable
fun SimpleChatView(
    activeMode: AiMode,
    messages: List<ChatMessage>,
    activeDocumentName: String?,
    onDocumentSelected: (String?) -> Unit,
    inputText: String,
    onInputTextChange: (String) -> Unit,
    isThinking: Boolean,
    onSendMessage: (String) -> Unit
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()

    // Document file picker for Documents RAG mode
    val documentPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val name = resolveFileName(context, uri)
            onDocumentSelected(name)
        }
    }

    val placeholderText = when (activeMode) {
        AiMode.DOCUMENT_RAG -> if (activeDocumentName != null) {
            "Ask about $activeDocumentName..."
        } else {
            "Attach document or ask question..."
        }
        AiMode.CHATBOT -> "Type a message..."
        AiMode.IMAGE_CREATION -> "Describe an image..."
    }

    LaunchedEffect(messages.size, isThinking) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PureBlack)
    ) {
        // Message timeline
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (messages.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = activeMode.title,
                            color = TextSecondary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )

                        if (activeMode == AiMode.DOCUMENT_RAG && activeDocumentName == null) {
                            Spacer(modifier = Modifier.height(14.dp))
                            Surface(
                                onClick = { documentPickerLauncher.launch("*/*") },
                                shape = RoundedCornerShape(20.dp),
                                color = SurfaceCard
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    LuxuryIcons.AttachmentIcon(
                                        modifier = Modifier.size(15.dp),
                                        color = PureWhite
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Upload PDF / Document",
                                        color = PureWhite,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(messages, key = { it.id }) { message ->
                        SimpleMessageBubble(message = message)
                    }

                    if (isThinking) {
                        item {
                            SimpleThinkingIndicator()
                        }
                    }
                }
            }
        }

        // Active document chip (if a document is attached)
        if (activeMode == AiMode.DOCUMENT_RAG && activeDocumentName != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceSelected
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LuxuryIcons.DocumentIcon(
                            modifier = Modifier.size(13.dp),
                            color = PureWhite
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = activeDocumentName,
                            color = PureWhite,
                            fontSize = 12.sp,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "✕",
                            color = TextMuted,
                            fontSize = 12.sp,
                            modifier = Modifier.clickable { onDocumentSelected(null) }
                        )
                    }
                }
            }
        }

        // Input bar
        Surface(
            color = PureBlack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // If in Documents RAG mode: show attachment button
                if (activeMode == AiMode.DOCUMENT_RAG) {
                    IconButton(
                        onClick = { documentPickerLauncher.launch("*/*") },
                        modifier = Modifier.size(40.dp)
                    ) {
                        LuxuryIcons.AttachmentIcon(
                            modifier = Modifier.size(18.dp),
                            color = PureWhite
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                }

                OutlinedTextField(
                    value = inputText,
                    onValueChange = onInputTextChange,
                    placeholder = {
                        Text(
                            text = placeholderText,
                            color = TextMuted,
                            fontSize = 14.sp
                        )
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = SurfaceCard,
                        unfocusedContainerColor = SurfaceCard,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = PureWhite
                    ),
                    maxLines = 4
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            onSendMessage(inputText.trim())
                        }
                    },
                    enabled = inputText.isNotBlank() && !isThinking,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PureWhite,
                        contentColor = PureBlack,
                        disabledContainerColor = SurfaceCard,
                        disabledContentColor = TextMuted
                    ),
                    modifier = Modifier.size(46.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    LuxuryIcons.ArrowUpIcon(
                        modifier = Modifier.size(16.dp),
                        color = if (inputText.isNotBlank() && !isThinking) PureBlack else TextMuted,
                        strokeWidth = 3.5f
                    )
                }
            }
        }
    }
}

private fun resolveFileName(context: Context, uri: Uri): String {
    var name = "document.pdf"
    try {
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1 && cursor.moveToFirst()) {
                name = cursor.getString(nameIndex)
            }
        }
    } catch (_: Exception) {}
    return name
}

@Composable
private fun SimpleMessageBubble(message: ChatMessage) {
    val isUser = message.isFromUser

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = if (isUser) Color(0xFF1C1C1E) else SurfaceCard
        ) {
            Text(
                text = message.text,
                color = PureWhite,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
            )
        }
    }
}

@Composable
private fun SimpleThinkingIndicator() {
    Row(
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(12.dp),
            color = PureWhite,
            strokeWidth = 2.dp
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Thinking...",
            color = TextSecondary,
            fontSize = 12.sp
        )
    }
}
