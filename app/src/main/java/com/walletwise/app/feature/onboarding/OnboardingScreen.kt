package com.walletwise.app.feature.onboarding

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.walletwise.app.core.designsystem.WalletPrimary
import com.walletwise.app.core.designsystem.WalletPrimaryDark
import com.walletwise.app.core.designsystem.WalletPrimaryLight
import com.walletwise.app.core.designsystem.components.PrimaryButton
import com.walletwise.app.core.designsystem.components.SecondaryButton

data class OnboardingPage(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val gradient: List<Color>
)

val onboardingPages = listOf(
    OnboardingPage(
        title = "Smart Expense Tracking",
        description = "Track your daily expenses seamlessly with real-time categorizations and automatic insight logging.",
        icon = Icons.Default.ReceiptLong,
        gradient = listOf(WalletPrimary, WalletPrimaryDark)
    ),
    OnboardingPage(
        title = "Intelligent Budget Discipline",
        description = "Set category-based monthly caps and receive instant threshold alerts before overspending occurs.",
        icon = Icons.Default.PieChart,
        gradient = listOf(Color(0xFF4338CA), WalletPrimary)
    ),
    OnboardingPage(
        title = "AI Predictive Financial Score",
        description = "Leverage smart forecast models to project future savings and elevate your financial discipline score.",
        icon = Icons.Default.AutoAwesome,
        gradient = listOf(Color(0xFF0F172A), Color(0xFF1E1B4B))
    )
)

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    onSkip: () -> Unit
) {
    var currentPage by remember { mutableStateOf(0) }
    val page = onboardingPages[currentPage]

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar with Skip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "WalletWise",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = WalletPrimary
                )
                if (currentPage < onboardingPages.size - 1) {
                    TextButton(onClick = onSkip) {
                        Text(
                            text = "Skip",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Center Animated Content
            AnimatedContent(
                targetState = currentPage,
                transitionSpec = {
                    slideInHorizontally { width -> width } + fadeIn() togetherWith
                            slideOutHorizontally { width -> -width } + fadeOut()
                },
                label = "onboarding_animation"
            ) { pageIdx ->
                val currentPageData = onboardingPages[pageIdx]
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    // Illustration Circle
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .clip(CircleShape)
                            .background(Brush.radialGradient(currentPageData.gradient)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = currentPageData.icon,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(90.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    Text(
                        text = currentPageData.title,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = currentPageData.description,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )
                }
            }

            // Bottom Controls
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Page Indicator Dots
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    onboardingPages.indices.forEach { index ->
                        Box(
                            modifier = Modifier
                                .height(8.dp)
                                .width(if (index == currentPage) 24.dp else 8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (index == currentPage) WalletPrimary
                                    else WalletPrimaryLight
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Action Buttons
                if (currentPage == onboardingPages.size - 1) {
                    PrimaryButton(
                        text = "Get Started",
                        onClick = onFinish
                    )
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        SecondaryButton(
                            text = "Skip",
                            onClick = onSkip,
                            modifier = Modifier.weight(1f)
                        )
                        PrimaryButton(
                            text = "Next",
                            onClick = { currentPage++ },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}
