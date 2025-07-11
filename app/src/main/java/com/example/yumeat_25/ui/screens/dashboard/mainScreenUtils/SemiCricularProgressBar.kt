package com.example.yumeat_25.ui.screens.dashboard.mainScreenUtils

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.*

@Composable
fun SemiCircularProgressBar(
    progress: Float, // 0f to 1f
    modifier: Modifier = Modifier,
    strokeWidth: Float = 16f,
    backgroundColor: Color = Color(0x11000000),
    progressColor: Color = Color.Black,
    drawEndDot: Boolean = true
) {
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val diameter = size.minDimension
            val arcRect = Size(diameter, diameter)
            // Draw background arc
            drawArc(
                color = backgroundColor,
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(0f, 0f),
                size = arcRect,
                style = Stroke(strokeWidth, cap = StrokeCap.Round)
            )
            // Draw progress arc
            drawArc(
                color = progressColor,
                startAngle = 180f,
                sweepAngle = 180f * progress.coerceIn(0f, 1f),
                useCenter = false,
                topLeft = Offset(0f, 0f),
                size = arcRect,
                style = Stroke(strokeWidth, cap = StrokeCap.Round)
            )
            // Draw end-dot if desired and progress > 0
            if (drawEndDot && progress > 0f) {
                val angleRad = Math.toRadians(180.0 + 180.0 * progress)
                val radius = diameter / 2
                val dotRadius = strokeWidth / 2
                val center = Offset(radius, radius)
                val dotCenter = Offset(
                    x = center.x + radius * cos(angleRad).toFloat(),
                    y = center.y + radius * sin(angleRad).toFloat()
                )
                drawCircle(
                    color = progressColor,
                    radius = dotRadius + 2,
                    center = dotCenter
                )
            }
        }
    }
}