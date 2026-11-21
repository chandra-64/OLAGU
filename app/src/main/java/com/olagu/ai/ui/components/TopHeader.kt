package com.olagu.ai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.olagu.ai.data.model.AiMode
import com.olagu.ai.data.model.AiModel
import com.olagu.ai.ui.theme.PureBlack
import com.olagu.ai.ui.theme.PureWhite
import com.olagu.ai.ui.theme.SurfaceCard
import com.olagu.ai.ui.theme.SurfaceSelected
import com.olagu.ai.ui.theme.TextPrimary
import com.olagu.ai.ui.theme.TextSecondary

/**
 * Clean Top Bar:
 * - Top Left: Menu button to open sidebar
 * - Center: Active mode title ("Data Analyzes", "Chatbot", "Image Creation")
 * - Top Right: Model selector ("Basic", "Balanced", "Advanced")
 */
@Composable
fun TopHeader(
    activeMode: AiMode,
    selectedModel: AiModel,
    onModelSelected: (AiModel) -> Unit,
    onOpenSidebar: () -> Unit
) {
    var isModelMenuExpanded by remember { mutableStateOf(false) }

    Surface(
        color = PureBlack,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Top Left: Menu Button and Title
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onOpenSidebar,
                    modifier = Modifier.size(36.dp)
                ) {
                    LuxuryIcons.MenuIcon(
                        modifier = Modifier.size(20.dp),
                        color = PureWhite
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = activeMode.title,
                    color = PureWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Top Right: Model Selector Dropdown
            Box {
                Surface(
                    onClick = { isModelMenuExpanded = true },
                    shape = RoundedCornerShape(18.dp),
                    color = SurfaceCard,
                    modifier = Modifier.height(36.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedModel.displayName,
                            color = PureWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        LuxuryIcons.ChevronDownIcon(
                            modifier = Modifier.size(10.dp),
                            color = TextSecondary
                        )
                    }
                }

                DropdownMenu(
                    expanded = isModelMenuExpanded,
                    onDismissRequest = { isModelMenuExpanded = false },
                    modifier = Modifier
                        .background(SurfaceCard)
                        .width(160.dp)
                ) {
                    AiModel.entries.forEach { model ->
                        val isSelected = model == selectedModel
                        DropdownMenuItem(
                            text = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = model.displayName,
                                        color = if (isSelected) PureWhite else TextPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                    if (isSelected) {
                                        LuxuryIcons.CheckIcon(
                                            modifier = Modifier.size(14.dp),
                                            color = PureWhite
                                        )
                                    }
                                }
                            },
                            onClick = {
                                onModelSelected(model)
                                isModelMenuExpanded = false
                            },
                            modifier = Modifier.background(
                                if (isSelected) SurfaceSelected else Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    }
}
