package com.olagu.ai.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.olagu.ai.ui.theme.PureWhite

/**
 * Premium custom vector icons rendered via Canvas.
 */
object LuxuryIcons {

    @Composable
    fun MenuIcon(
        modifier: Modifier = Modifier.size(20.dp),
        color: Color = PureWhite,
        strokeWidth: Float = 4f
    ) {
        Canvas(modifier = modifier) {
            val w = size.width
            val h = size.height

            drawLine(
                color = color,
                start = Offset(0f, h * 0.25f),
                end = Offset(w, h * 0.25f),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
            drawLine(
                color = color,
                start = Offset(0f, h * 0.5f),
                end = Offset(w * 0.72f, h * 0.5f),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
            drawLine(
                color = color,
                start = Offset(0f, h * 0.75f),
                end = Offset(w, h * 0.75f),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
        }
    }

    @Composable
    fun PlusIcon(
        modifier: Modifier = Modifier.size(18.dp),
        color: Color = PureWhite,
        strokeWidth: Float = 4f
    ) {
        Canvas(modifier = modifier) {
            val w = size.width
            val h = size.height
            val cx = w / 2f
            val cy = h / 2f

            drawLine(
                color = color,
                start = Offset(cx, h * 0.15f),
                end = Offset(cx, h * 0.85f),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
            drawLine(
                color = color,
                start = Offset(w * 0.15f, cy),
                end = Offset(w * 0.85f, cy),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
        }
    }

    @Composable
    fun DocumentIcon(
        modifier: Modifier = Modifier.size(20.dp),
        color: Color = PureWhite,
        strokeWidth: Float = 3f
    ) {
        Canvas(modifier = modifier) {
            val w = size.width
            val h = size.height

            // Page outline with folded top-right corner
            val path = Path().apply {
                moveTo(w * 0.2f, h * 0.1f)
                lineTo(w * 0.62f, h * 0.1f)
                lineTo(w * 0.82f, h * 0.3f)
                lineTo(w * 0.82f, h * 0.9f)
                lineTo(w * 0.2f, h * 0.9f)
                close()
            }
            drawPath(path = path, color = color, style = Stroke(width = strokeWidth))

            // Corner fold line
            val fold = Path().apply {
                moveTo(w * 0.62f, h * 0.1f)
                lineTo(w * 0.62f, h * 0.3f)
                lineTo(w * 0.82f, h * 0.3f)
            }
            drawPath(path = fold, color = color, style = Stroke(width = strokeWidth))

            // Document text lines
            drawLine(
                color = color,
                start = Offset(w * 0.35f, h * 0.48f),
                end = Offset(w * 0.68f, h * 0.48f),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
            drawLine(
                color = color,
                start = Offset(w * 0.35f, h * 0.64f),
                end = Offset(w * 0.68f, h * 0.64f),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
        }
    }

    @Composable
    fun ChatbotIcon(
        modifier: Modifier = Modifier.size(20.dp),
        color: Color = PureWhite,
        strokeWidth: Float = 3.5f
    ) {
        Canvas(modifier = modifier) {
            val w = size.width
            val h = size.height

            val path = Path().apply {
                moveTo(w * 0.15f, h * 0.2f)
                lineTo(w * 0.85f, h * 0.2f)
                cubicTo(w * 0.95f, h * 0.2f, w * 0.95f, h * 0.65f, w * 0.85f, h * 0.65f)
                lineTo(w * 0.45f, h * 0.65f)
                lineTo(w * 0.25f, h * 0.85f)
                lineTo(w * 0.25f, h * 0.65f)
                lineTo(w * 0.15f, h * 0.65f)
                cubicTo(w * 0.05f, h * 0.65f, w * 0.05f, h * 0.2f, w * 0.15f, h * 0.2f)
                close()
            }
            drawPath(
                path = path,
                color = color,
                style = Stroke(width = strokeWidth)
            )

            drawCircle(color = color, radius = 2.5f, center = Offset(w * 0.35f, h * 0.42f))
            drawCircle(color = color, radius = 2.5f, center = Offset(w * 0.50f, h * 0.42f))
            drawCircle(color = color, radius = 2.5f, center = Offset(w * 0.65f, h * 0.42f))
        }
    }

    @Composable
    fun ImageCreationIcon(
        modifier: Modifier = Modifier.size(20.dp),
        color: Color = PureWhite,
        strokeWidth: Float = 3.5f
    ) {
        Canvas(modifier = modifier) {
            val w = size.width
            val h = size.height

            drawRoundRect(
                color = color,
                topLeft = Offset(w * 0.1f, h * 0.15f),
                size = Size(w * 0.8f, h * 0.7f),
                cornerRadius = CornerRadius(6f, 6f),
                style = Stroke(width = strokeWidth)
            )

            drawCircle(
                color = color,
                radius = w * 0.09f,
                center = Offset(w * 0.32f, h * 0.38f)
            )

            val mountainPath = Path().apply {
                moveTo(w * 0.15f, h * 0.75f)
                lineTo(w * 0.42f, h * 0.48f)
                lineTo(w * 0.58f, h * 0.62f)
                lineTo(w * 0.72f, h * 0.52f)
                lineTo(w * 0.85f, h * 0.75f)
            }
            drawPath(
                path = mountainPath,
                color = color,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
    }

    @Composable
    fun AttachmentIcon(
        modifier: Modifier = Modifier.size(18.dp),
        color: Color = PureWhite,
        strokeWidth: Float = 3f
    ) {
        Canvas(modifier = modifier) {
            val w = size.width
            val h = size.height

            // Minimal paperclip path
            val path = Path().apply {
                moveTo(w * 0.35f, h * 0.45f)
                lineTo(w * 0.35f, h * 0.7f)
                cubicTo(w * 0.35f, h * 0.88f, w * 0.65f, h * 0.88f, w * 0.65f, h * 0.7f)
                lineTo(w * 0.65f, h * 0.3f)
                cubicTo(w * 0.65f, h * 0.12f, w * 0.2f, h * 0.12f, w * 0.2f, h * 0.3f)
                lineTo(w * 0.2f, h * 0.75f)
            }
            drawPath(path = path, color = color, style = Stroke(width = strokeWidth, cap = StrokeCap.Round))
        }
    }

    @Composable
    fun ChevronDownIcon(
        modifier: Modifier = Modifier.size(14.dp),
        color: Color = PureWhite,
        strokeWidth: Float = 3.5f
    ) {
        Canvas(modifier = modifier) {
            val w = size.width
            val h = size.height
            val path = Path().apply {
                moveTo(w * 0.2f, h * 0.35f)
                lineTo(w * 0.5f, h * 0.65f)
                lineTo(w * 0.8f, h * 0.35f)
            }
            drawPath(
                path = path,
                color = color,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
    }

    @Composable
    fun CheckIcon(
        modifier: Modifier = Modifier.size(16.dp),
        color: Color = PureWhite,
        strokeWidth: Float = 3.5f
    ) {
        Canvas(modifier = modifier) {
            val w = size.width
            val h = size.height
            val path = Path().apply {
                moveTo(w * 0.15f, h * 0.5f)
                lineTo(w * 0.42f, h * 0.78f)
                lineTo(w * 0.88f, h * 0.25f)
            }
            drawPath(
                path = path,
                color = color,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
    }

    @Composable
    fun ArrowUpIcon(
        modifier: Modifier = Modifier.size(18.dp),
        color: Color = PureWhite,
        strokeWidth: Float = 4f
    ) {
        Canvas(modifier = modifier) {
            val w = size.width
            val h = size.height
            val cx = w / 2f

            drawLine(
                color = color,
                start = Offset(cx, h * 0.8f),
                end = Offset(cx, h * 0.2f),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
            val path = Path().apply {
                moveTo(w * 0.25f, h * 0.45f)
                lineTo(cx, h * 0.2f)
                lineTo(w * 0.75f, h * 0.45f)
            }
            drawPath(
                path = path,
                color = color,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
    }

    @Composable
    fun TrashIcon(
        modifier: Modifier = Modifier.size(16.dp),
        color: Color = PureWhite,
        strokeWidth: Float = 2.8f
    ) {
        Canvas(modifier = modifier) {
            val w = size.width
            val h = size.height

            drawLine(
                color = color,
                start = Offset(w * 0.15f, h * 0.25f),
                end = Offset(w * 0.85f, h * 0.25f),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
            drawArc(
                color = color,
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(w * 0.35f, h * 0.12f),
                size = Size(w * 0.3f, h * 0.2f),
                style = Stroke(width = strokeWidth)
            )
            val path = Path().apply {
                moveTo(w * 0.25f, h * 0.25f)
                lineTo(w * 0.28f, h * 0.85f)
                lineTo(w * 0.72f, h * 0.85f)
                lineTo(w * 0.75f, h * 0.25f)
            }
            drawPath(
                path = path,
                color = color,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
    }
}
