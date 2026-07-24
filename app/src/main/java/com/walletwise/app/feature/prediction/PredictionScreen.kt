package com.walletwise.app.feature.prediction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.walletwise.app.core.designsystem.*
import com.walletwise.app.core.designsystem.components.*

@Composable
fun PredictionScreen(
    viewModel: PredictionViewModel = PredictionViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val pred = uiState.prediction

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(WalletBackground),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "AI Spending Forecast",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = WalletTextPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Rounded.AutoAwesome,
                        contentDescription = "AI",
                        tint = WalletAccentOrange,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = "Powered by Gemini predictive financial model",
                    fontSize = 13.sp,
                    color = WalletTextSecondary
                )
            }
        }

        // Confidence Card & Hero Stats
        item {
            GradientWalletCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "NEXT 30 DAYS FORECAST",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "₹${pred?.forecastedSpending?.toInt() ?: 38450}",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(ChipShape)
                            .background(Color.White.copy(alpha = 0.2f))
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "${pred?.confidenceScore ?: 94}% Confidence",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Expected Monthly Savings: ₹${pred?.expectedSavings?.toInt() ?: 14200}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }

        // AI Smart Recommendations
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "AI Smart Recommendations",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = WalletTextPrimary,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        items(pred?.aiRecommendations ?: emptyList()) { recommendation ->
            RecommendationItem(
                text = recommendation,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
            )
        }

        // Predicted Category Breakdown Card
        item {
            Spacer(modifier = Modifier.height(20.dp))
            WalletCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                elevation = 4.dp
            ) {
                Text(
                    text = "Predicted Category Distribution",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = WalletTextPrimary
                )
                Spacer(modifier = Modifier.height(12.dp))

                pred?.predictedCategoryBreakdown?.forEach { (category, amount) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(category, fontSize = 14.sp, color = WalletTextSecondary)
                        Text("₹${amount.toInt()}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = WalletTextPrimary)
                    }
                }
            }
        }
    }
}

@Composable
private fun RecommendationItem(
    text: String,
    modifier: Modifier = Modifier
) {
    WalletCard(
        modifier = modifier,
        elevation = 2.dp
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFF3E0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Lightbulb,
                    contentDescription = "Tip",
                    tint = WalletAccentOrange,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = text,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = WalletTextPrimary
            )
        }
    }
}
