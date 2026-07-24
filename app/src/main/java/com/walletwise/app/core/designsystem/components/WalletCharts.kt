package com.walletwise.app.core.designsystem.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.walletwise.app.core.designsystem.*

@Composable
fun CircularBudgetProgress(
    percentage: Float,
    spentText: String,
    totalText: String,
    modifier: Modifier = Modifier,
    primaryColor: Color = WalletPrimary,
    backgroundColor: Color = WalletDivider
) {
    val animatedProgress by animateFloatAsState(
        targetValue = percentage.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 1200),
        label = "CircularProgress"
    )

    Box(
        modifier = modifier.size(180.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 16.dp.toPx()
            val arcSize = size.minDimension - strokeWidth

            // Track arc
            drawArc(
                color = backgroundColor,
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                size = Size(arcSize, arcSize),
                topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
            )

            // Progress arc
            drawArc(
                color = primaryColor,
                startAngle = 135f,
                sweepAngle = 270f * animatedProgress,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                size = Size(arcSize, arcSize),
                topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Spent",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = WalletTextSecondary
            )
            Text(
                text = spentText,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = WalletTextPrimary
            )
            Text(
                text = "of $totalText",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = WalletTextSecondary
            )
        }
    }
}

@Composable
fun BarChartWidget(
    data: Map<String, Float>, // Label to Float value 0..1
    modifier: Modifier = Modifier,
    barColor: Color = WalletPrimary
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        data.forEach { (label, value) ->
            val animatedHeight by animateFloatAsState(
                targetValue = value.coerceIn(0.1f, 1f),
                animationSpec = tween(1000),
                label = "BarHeight"
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .width(20.dp)
                        .fillMaxHeight(animatedHeight)
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                        .background(if (value > 0.8f) WalletAccentCoral else barColor)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = label,
                    fontSize = 11.sp,
                    color = WalletTextSecondary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun CategoryProgressRow(
    categoryName: String,
    spentAmount: String,
    allocatedAmount: String,
    progress: Float,
    color: Color = WalletPrimary
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(categoryName, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = WalletTextPrimary)
            Text("$spentAmount / $allocatedAmount", fontSize = 13.sp, color = WalletTextSecondary)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape)
                .background(WalletDivider)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .clip(CircleShape)
                    .background(if (progress > 0.9f) WalletError else color)
            )
        }
    }
}
