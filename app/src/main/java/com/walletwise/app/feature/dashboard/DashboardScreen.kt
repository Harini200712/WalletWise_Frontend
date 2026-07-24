package com.walletwise.app.feature.dashboard

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.walletwise.app.core.designsystem.*
import com.walletwise.app.core.designsystem.components.*
import com.walletwise.app.core.model.Expense

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = DashboardViewModel(),
    onNavigateExpenses: () -> Unit,
    onNavigateBudget: () -> Unit,
    onNavigatePrediction: () -> Unit,
    onNavigateNotifications: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(WalletBackground),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Greeting Header
        item {
            HeaderGreetingRow(
                userName = uiState.user?.name ?: "Alex",
                onNotificationClick = onNavigateNotifications
            )
        }

        // Wallet Score Card
        item {
            Spacer(modifier = Modifier.height(16.dp))
            WalletScoreCard(
                score = uiState.score?.score ?: 785,
                maxScore = uiState.score?.maxScore ?: 900,
                rating = uiState.score?.ratingLabel ?: "Excellent",
                summary = uiState.score?.summaryMessage ?: "Top financial discipline!"
            )
        }

        // Monthly Summary & Quick Stats
        item {
            Spacer(modifier = Modifier.height(20.dp))
            MonthlyOverviewSection(
                spent = uiState.totalExpenseMonth,
                budget = uiState.totalBudgetMonth,
                savings = uiState.savingsMonth
            )
        }

        // Quick Actions Row
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Quick Actions",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = WalletTextPrimary,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            QuickActionsRow(
                onNavigateExpenses = onNavigateExpenses,
                onNavigateBudget = onNavigateBudget,
                onNavigatePrediction = onNavigatePrediction
            )
        }

        // AI Insights Widget
        item {
            Spacer(modifier = Modifier.height(24.dp))
            AiInsightsCard(
                insightTitle = uiState.insights.firstOrNull()?.title ?: "Weekend Spending Alert",
                insightDesc = uiState.insights.firstOrNull()?.description ?: "Save up to ₹3,200 monthly by managing dining orders.",
                onActionClick = onNavigatePrediction
            )
        }

        // Recent Expenses List Section
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Transactions",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = WalletTextPrimary
                )
                Text(
                    text = "View All",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = WalletPrimary,
                    modifier = Modifier.clickable { onNavigateExpenses() }
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        items(uiState.recentExpenses) { expense ->
            DashboardExpenseItem(
                expense = expense,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
private fun HeaderGreetingRow(
    userName: String,
    onNotificationClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Welcome back, $userName",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = WalletTextSecondary
            )
            Text(
                text = "Financial Overview",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = WalletTextPrimary
            )
        }

        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(WalletSurface)
                .clickable { onNotificationClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.NotificationsNone,
                contentDescription = "Notifications",
                tint = WalletTextPrimary
            )
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(WalletAccentCoral)
                    .align(Alignment.TopEnd)
                    .offset(x = (-2).dp, y = 2.dp)
            )
        }
    }
}

@Composable
private fun WalletScoreCard(
    score: Int,
    maxScore: Int,
    rating: String,
    summary: String
) {
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Shield,
                        contentDescription = "Score",
                        tint = WalletAccentOrange,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "WALLET SCORE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.8f),
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$score",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = " / $maxScore",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(ChipShape)
                    .background(Color.White.copy(alpha = 0.2f))
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = rating,
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
            text = summary,
            fontSize = 13.sp,
            color = Color.White.copy(alpha = 0.9f)
        )
    }
}

@Composable
private fun MonthlyOverviewSection(
    spent: Double,
    budget: Double,
    savings: Double
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        StatCard(
            title = "Monthly Spent",
            value = "₹${spent.toInt()}",
            subtitle = "Budget: ₹${budget.toInt()}",
            modifier = Modifier.weight(1f),
            accentColor = WalletAccentCoral
        )

        StatCard(
            title = "Est. Savings",
            value = "₹${savings.toInt()}",
            subtitle = "+12% vs last mo",
            modifier = Modifier.weight(1f),
            accentColor = WalletSuccess
        )
    }
}

@Composable
private fun QuickActionsRow(
    onNavigateExpenses: () -> Unit,
    onNavigateBudget: () -> Unit,
    onNavigatePrediction: () -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            ActionButtonTile(
                title = "Expenses",
                icon = Icons.Rounded.ReceiptLong,
                bgColor = WalletPrimaryLight,
                iconColor = WalletPrimary,
                onClick = onNavigateExpenses
            )
        }
        item {
            ActionButtonTile(
                title = "Budgets",
                icon = Icons.Rounded.AccountBalanceWallet,
                bgColor = Color(0xFFE8F5E9),
                iconColor = WalletSuccess,
                onClick = onNavigateBudget
            )
        }
        item {
            ActionButtonTile(
                title = "AI Forecast",
                icon = Icons.Rounded.AutoAwesome,
                bgColor = Color(0xFFFFF3E0),
                iconColor = WalletAccentOrange,
                onClick = onNavigatePrediction
            )
        }
    }
}

@Composable
private fun ActionButtonTile(
    title: String,
    icon: ImageVector,
    bgColor: Color,
    iconColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(TextFieldShape)
            .background(WalletSurface)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = WalletTextPrimary
        )
    }
}

@Composable
private fun AiInsightsCard(
    insightTitle: String,
    insightDesc: String,
    onActionClick: () -> Unit
) {
    WalletCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        backgroundColor = WalletSurface,
        elevation = 6.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.AutoAwesome,
                    contentDescription = "AI",
                    tint = WalletAccentOrange,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "AI INSIGHT OF THE DAY",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = WalletAccentOrange
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = insightTitle,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = WalletTextPrimary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = insightDesc,
            fontSize = 13.sp,
            color = WalletTextSecondary
        )
    }
}

@Composable
private fun DashboardExpenseItem(
    expense: Expense,
    modifier: Modifier = Modifier
) {
    WalletCard(
        modifier = modifier,
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(WalletPrimaryLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ShoppingBag,
                        contentDescription = expense.title,
                        tint = WalletPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = expense.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = WalletTextPrimary
                    )
                    Text(
                        text = "${expense.merchant} • ${expense.date}",
                        fontSize = 12.sp,
                        color = WalletTextSecondary
                    )
                }
            }

            Text(
                text = "-₹${expense.amount.toInt()}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = WalletTextPrimary
            )
        }
    }
}
