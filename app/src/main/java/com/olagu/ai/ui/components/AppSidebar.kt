package com.olagu.ai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.olagu.ai.data.model.AiMode
import com.olagu.ai.data.model.ChatSession
import com.olagu.ai.ui.theme.PureBlack
import com.olagu.ai.ui.theme.PureWhite
import com.olagu.ai.ui.theme.SurfaceSelected
import com.olagu.ai.ui.theme.TextMuted
import com.olagu.ai.ui.theme.TextPrimary
import com.olagu.ai.ui.theme.TextSecondary
import com.olagu.ai.ui.theme.TextTertiary
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Minimalist sidebar:
 * - New Chat button at top
 * - 3 Options: Documents RAG, Chatbot, Image Creation
 * - History maintained with particular pages (filtered per mode/page, not for every response)
 * - Grouped by date sections with a minimal line design between days
 */
@Composable
fun AppSidebar(
    activeMode: AiMode,
    activeSessionId: String?,
    historyForCurrentPage: List<ChatSession>,
    onSelectMode: (AiMode) -> Unit,
    onSelectSession: (ChatSession) -> Unit,
    onNewChat: () -> Unit,
    onDeleteSession: (String) -> Unit,
    onCloseSidebar: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxHeight()
            .width(300.dp)
            .statusBarsPadding(),
        color = PureBlack
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp)
        ) {
            // App Title & Close
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "OLAGU",
                    color = PureWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                IconButton(
                    onClick = onCloseSidebar,
                    modifier = Modifier.size(36.dp)
                ) {
                    Text(text = "✕", color = TextSecondary, fontSize = 16.sp)
                }
            }

            // "New Chat" Option right at top
            Button(
                onClick = {
                    onNewChat()
                    onCloseSidebar()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PureWhite,
                    contentColor = PureBlack
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    LuxuryIcons.PlusIcon(
                        modifier = Modifier.size(16.dp),
                        color = PureBlack,
                        strokeWidth = 3.5f
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "New Chat",
                        color = PureBlack,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3 Mode Options
            SidebarOptionItem(
                title = "Documents RAG",
                isSelected = activeMode == AiMode.DOCUMENT_RAG,
                icon = {
                    LuxuryIcons.DocumentIcon(
                        color = if (activeMode == AiMode.DOCUMENT_RAG) PureWhite else TextSecondary
                    )
                },
                onClick = {
                    onSelectMode(AiMode.DOCUMENT_RAG)
                    onCloseSidebar()
                }
            )

            Spacer(modifier = Modifier.height(4.dp))

            SidebarOptionItem(
                title = "Chatbot",
                isSelected = activeMode == AiMode.CHATBOT,
                icon = {
                    LuxuryIcons.ChatbotIcon(
                        color = if (activeMode == AiMode.CHATBOT) PureWhite else TextSecondary
                    )
                },
                onClick = {
                    onSelectMode(AiMode.CHATBOT)
                    onCloseSidebar()
                }
            )

            Spacer(modifier = Modifier.height(4.dp))

            SidebarOptionItem(
                title = "Image Creation",
                isSelected = activeMode == AiMode.IMAGE_CREATION,
                icon = {
                    LuxuryIcons.ImageCreationIcon(
                        color = if (activeMode == AiMode.IMAGE_CREATION) PureWhite else TextSecondary
                    )
                },
                onClick = {
                    onSelectMode(AiMode.IMAGE_CREATION)
                    onCloseSidebar()
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // History header label for the particular page
            Text(
                text = "${activeMode.title} History",
                color = TextTertiary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // History of conversation pages for this particular page
            if (historyForCurrentPage.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No saved pages yet",
                        color = TextMuted,
                        fontSize = 13.sp
                    )
                }
            } else {
                val groupedHistory = remember(historyForCurrentPage) {
                    historyForCurrentPage.groupBy { getDaySection(it.createdAt) }
                }

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    var sectionIndex = 0
                    groupedHistory.forEach { (sectionTitle, sessions) ->
                        // Minimal line divider between day sections
                        if (sectionIndex > 0) {
                            item(key = "divider_$sectionTitle") {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 10.dp, horizontal = 6.dp)
                                        .height(1.dp)
                                        .background(Color(0x18FFFFFF))
                                )
                            }
                        }

                        // Date section title (e.g. Today, Yesterday)
                        item(key = "header_$sectionTitle") {
                            Text(
                                text = sectionTitle,
                                color = TextMuted,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(start = 8.dp, top = 6.dp, bottom = 4.dp)
                            )
                        }

                        // Each item represents a full conversation page
                        items(sessions, key = { it.id }) { session ->
                            HistoryRowItem(
                                session = session,
                                isSelected = session.id == activeSessionId,
                                onClick = {
                                    onSelectSession(session)
                                    onCloseSidebar()
                                },
                                onDelete = {
                                    onDeleteSession(session.id)
                                }
                            )
                        }

                        sectionIndex++
                    }
                }
            }
        }
    }
}

private fun getDaySection(timeMillis: Long): String {
    val now = Calendar.getInstance()
    val sessionCal = Calendar.getInstance().apply { timeInMillis = timeMillis }

    val sameYear = now.get(Calendar.YEAR) == sessionCal.get(Calendar.YEAR)
    val dayDiff = now.get(Calendar.DAY_OF_YEAR) - sessionCal.get(Calendar.DAY_OF_YEAR)

    return when {
        sameYear && dayDiff == 0 -> "Today"
        sameYear && dayDiff == 1 -> "Yesterday"
        sameYear && dayDiff in 2..7 -> SimpleDateFormat("EEEE", Locale.getDefault()).format(Date(timeMillis))
        sameYear -> SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(timeMillis))
        else -> SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(timeMillis))
    }
}

@Composable
private fun SidebarOptionItem(
    title: String,
    isSelected: Boolean,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) SurfaceSelected else PureBlack,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = title,
                color = if (isSelected) PureWhite else TextPrimary,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
private fun HistoryRowItem(
    session: ChatSession,
    isSelected: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) SurfaceSelected else PureBlack,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = session.title,
                color = if (isSelected) PureWhite else TextSecondary,
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(28.dp)
            ) {
                LuxuryIcons.TrashIcon(
                    modifier = Modifier.size(13.dp),
                    color = TextMuted
                )
            }
        }
    }
}
