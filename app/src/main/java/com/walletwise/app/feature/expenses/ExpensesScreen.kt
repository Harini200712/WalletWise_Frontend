package com.walletwise.app.feature.expenses

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
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
import com.walletwise.app.core.model.Expense
import com.walletwise.app.core.model.ExpenseSortOption
import kotlinx.coroutines.launch

@Composable
fun ExpensesScreen(
    viewModel: ExpensesViewModel = viewModel(),
    onNavigateExpenseDetail: (String) -> Unit,
    onOpenAddExpense: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showSortMenu by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onOpenAddExpense,
                containerColor = WalletPrimary,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Add Expense")
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Screen Header & Sorting Button
            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Expense History",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "${uiState.filteredExpenses.size} transactions recorded",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Box {
                        IconButton(
                            onClick = { showSortMenu = true },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Sort,
                                contentDescription = "Sort",
                                tint = WalletPrimary
                            )
                        }

                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false },
                            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                        ) {
                            ExpenseSortOption.values().forEach { option ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = option.displayName,
                                            fontWeight = if (option == uiState.sortOption) FontWeight.Bold else FontWeight.Normal,
                                            color = if (option == uiState.sortOption) WalletPrimary else MaterialTheme.colorScheme.onSurface
                                        )
                                    },
                                    onClick = {
                                        viewModel.onSortOptionSelected(option)
                                        showSortMenu = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                SearchBar(
                    query = uiState.searchQuery,
                    onQueryChange = viewModel::onSearchQueryChanged
                )
            }

            // Category Filter Chips
            FilterChipRow(
                categories = uiState.categories,
                selectedCategory = uiState.selectedCategory,
                onCategorySelected = viewModel::onCategorySelected,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Expense List
            if (uiState.filteredExpenses.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Rounded.ReceiptLong,
                            contentDescription = "Empty",
                            tint = WalletTextSecondary,
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No matching expenses found",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(uiState.filteredExpenses, key = { it.id }) { expense ->
                        ExpenseListItem(
                            expense = expense,
                            onClick = { onNavigateExpenseDetail(expense.id) },
                            onDelete = {
                                viewModel.deleteExpense(expense.id)
                                coroutineScope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "Expense removed",
                                        actionLabel = "Undo",
                                        duration = SnackbarDuration.Short
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        viewModel.undoLastDelete()
                                    }
                                }
                            },
                            onDuplicate = { viewModel.duplicateExpense(expense.id) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ExpenseListItem(
    expense: Expense,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onDuplicate: () -> Unit
) {
    var showContextMenu by remember { mutableStateOf(false) }

    Box {
        WalletCard(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = { showContextMenu = true }
                ),
            elevation = 3.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(WalletPrimaryLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ReceiptLong,
                            contentDescription = expense.category.displayName,
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
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${expense.merchant} • ${expense.category.displayName}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (expense.notes.isNotBlank()) {
                            Text(
                                text = "Note: ${expense.notes}",
                                fontSize = 11.sp,
                                color = WalletPrimary
                            )
                        }
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "-₹${expense.amount.toInt()}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        IconButton(onClick = onDuplicate, modifier = Modifier.size(24.dp)) {
                            Icon(
                                imageVector = Icons.Rounded.ContentCopy,
                                contentDescription = "Duplicate",
                                tint = WalletPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                            Icon(
                                imageVector = Icons.Rounded.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        DropdownMenu(
            expanded = showContextMenu,
            onDismissRequest = { showContextMenu = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
        ) {
            DropdownMenuItem(
                text = { Text("View Details", color = MaterialTheme.colorScheme.onSurface) },
                onClick = {
                    showContextMenu = false
                    onClick()
                }
            )
            DropdownMenuItem(
                text = { Text("Duplicate Transaction", color = WalletPrimary) },
                onClick = {
                    showContextMenu = false
                    onDuplicate()
                }
            )
            DropdownMenuItem(
                text = { Text("Delete Expense", color = MaterialTheme.colorScheme.error) },
                onClick = {
                    showContextMenu = false
                    onDelete()
                }
            )
        }
    }
}
