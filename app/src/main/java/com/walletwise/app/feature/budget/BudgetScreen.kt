package com.walletwise.app.feature.budget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Warning
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
import com.walletwise.app.core.model.Budget

@Composable
fun BudgetScreen(
    viewModel: BudgetViewModel = viewModel(),
    onOpenCreateBudget: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedBudgetForEdit by remember { mutableStateOf<Budget?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onOpenCreateBudget,
                containerColor = WalletPrimary,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Create Budget")
            }
        },
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
                        text = "Budget Planner",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Set category caps & prevent monthly overspending",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Circular Budget Gauge Widget
            item {
                WalletCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    elevation = 6.dp
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CircularBudgetProgress(
                            percentage = uiState.overallPercentage,
                            spentText = "₹${uiState.totalSpent.toInt()}",
                            totalText = "₹${uiState.totalAllocated.toInt()}"
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Box(
                            modifier = Modifier
                                .clip(ChipShape)
                                .background(WalletPrimaryLight)
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "Remaining: ₹${(uiState.totalAllocated - uiState.totalSpent).coerceAtLeast(0.0).toInt()}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = WalletPrimary
                            )
                        }
                    }
                }
            }

            // Overspending Alert Banner
            if (uiState.overspentCount > 0) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    WalletCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        backgroundColor = Color(0xFFFFF0F0),
                        elevation = 2.dp
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.Warning,
                                contentDescription = "Warning",
                                tint = WalletError,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Overspending Alert",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = WalletError
                                )
                                Text(
                                    text = "${uiState.overspentCount} categories have exceeded allocated caps.",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Category Budget Breakdown",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            items(uiState.budgets, key = { it.id }) { budget ->
                CategoryBudgetCard(
                    budget = budget,
                    onClick = { selectedBudgetForEdit = budget },
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
                )
            }
        }
    }

    if (selectedBudgetForEdit != null) {
        CreateEditBudgetSheet(
            onDismiss = { selectedBudgetForEdit = null },
            initialBudget = selectedBudgetForEdit,
            onSaveBudget = { updated ->
                viewModel.updateBudget(updated)
                selectedBudgetForEdit = null
            },
            onDeleteBudget = { id ->
                viewModel.deleteBudget(id)
                selectedBudgetForEdit = null
            }
        )
    }
}

@Composable
private fun CategoryBudgetCard(
    budget: Budget,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    WalletCard(
        modifier = modifier.clickable { onClick() },
        elevation = 3.dp
    ) {
        CategoryProgressRow(
            categoryName = budget.category.displayName,
            spentAmount = "₹${budget.spentAmount.toInt()}",
            allocatedAmount = "₹${budget.allocatedAmount.toInt()}",
            progress = budget.progressPercentage,
            color = if (budget.isOverspent) WalletError else WalletPrimary
        )
    }
}
