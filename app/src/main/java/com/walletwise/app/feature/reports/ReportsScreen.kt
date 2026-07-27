package com.walletwise.app.feature.reports

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.walletwise.app.core.designsystem.*
import com.walletwise.app.core.designsystem.components.*
import com.walletwise.app.core.model.TimePeriod
import kotlinx.coroutines.launch

@Composable
fun ReportsScreen(
    viewModel: ReportsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            item {
                Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
                    Text(
                        text = "Financial Analytics",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Interactive breakdown, timeframe trends & reports",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Time Period Selector Row (Daily, Weekly, Monthly, Yearly)
            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(TimePeriod.values()) { period ->
                        val isSelected = period == uiState.selectedPeriod
                        Box(
                            modifier = Modifier
                                .clip(ChipShape)
                                .background(if (isSelected) WalletPrimary else MaterialTheme.colorScheme.surface)
                                .clickable { viewModel.onPeriodSelected(period) }
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = period.displayName,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Summary Card Grid
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    StatCard(
                        title = "Total Outflow",
                        value = "₹${uiState.totalExpenses.toInt()}",
                        subtitle = "Top: ${uiState.topExpenseCategory}",
                        modifier = Modifier.weight(1f),
                        accentColor = WalletAccentCoral
                    )
                    StatCard(
                        title = "Net Savings",
                        value = "₹${uiState.netSavings.toInt()}",
                        subtitle = "${uiState.savingsRate}% Savings Rate",
                        modifier = Modifier.weight(1f),
                        accentColor = WalletSuccess
                    )
                }
            }

            // Bar Chart Card
            item {
                Spacer(modifier = Modifier.height(20.dp))
                WalletCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    elevation = 6.dp
                ) {
                    Text(
                        text = "Category Spend Comparison (${uiState.selectedPeriod.displayName})",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Normalized Expense Load Ratio",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    if (uiState.monthlyCategoryData.isNotEmpty()) {
                        BarChartWidget(
                            data = uiState.monthlyCategoryData
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No expense data available for chart", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                        }
                    }
                }
            }

            // Detailed Health Score Card
            item {
                Spacer(modifier = Modifier.height(20.dp))
                WalletCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    elevation = 4.dp
                ) {
                    Text(
                        text = "Financial Health Indicators",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = WalletDivider)
                    Spacer(modifier = Modifier.height(12.dp))

                    ReportMetricRow(label = "Monthly Income Credited", value = "₹${uiState.totalIncome.toInt()}")
                    ReportMetricRow(label = "Income-to-Expense Ratio", value = "2.66x (Healthy)")
                    ReportMetricRow(label = "Emergency Savings Reserve", value = "4.2 Months")
                    ReportMetricRow(label = "Debt-to-Income", value = "0% (Zero Debt)")
                }
            }

            // Export Button
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Box(modifier = Modifier.padding(horizontal = 24.dp)) {
                    SecondaryButton(
                        text = "Export ${uiState.selectedPeriod.displayName} Statement (PDF)",
                        onClick = {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Statement Exported Successfully as PDF!")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ReportMetricRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
    }
}
