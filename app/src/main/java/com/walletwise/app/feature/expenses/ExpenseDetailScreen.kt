package com.walletwise.app.feature.expenses

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import com.walletwise.app.core.designsystem.*
import com.walletwise.app.core.designsystem.components.PrimaryButton
import com.walletwise.app.core.designsystem.components.SecondaryButton
import com.walletwise.app.core.designsystem.components.WalletCard
import com.walletwise.app.core.model.Expense
import com.walletwise.app.data.repository.WalletRepository

@Composable
fun ExpenseDetailScreen(
    expenseId: String,
    onBack: () -> Unit,
    repository: WalletRepository = remember { WalletRepository() }
) {
    val expenses by repository.expenses.collectAsState(initial = emptyList())
    val userProfile by repository.user.collectAsState(initial = com.walletwise.app.core.model.UserProfile())
    val expense = expenses.firstOrNull { it.id == expenseId } ?: expenses.firstOrNull()

    var showEditSheet by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    if (expense == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Transaction not found", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(16.dp))
                PrimaryButton(text = "Back", onClick = onBack, modifier = Modifier.width(120.dp))
            }
        }
        return
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Transaction Details",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Row {
                    IconButton(onClick = { repository.duplicateExpense(expense.id) }) {
                        Icon(Icons.Rounded.ContentCopy, contentDescription = "Duplicate", tint = WalletPrimary)
                    }
                    IconButton(onClick = { showEditSheet = true }) {
                        Icon(Icons.Rounded.Edit, contentDescription = "Edit", tint = WalletPrimary)
                    }
                    IconButton(onClick = {
                        repository.deleteExpense(expense.id)
                        onBack()
                    }) {
                        Icon(Icons.Rounded.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            WalletCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 6.dp
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(WalletPrimaryLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ReceiptLong,
                            contentDescription = "Merchant",
                            tint = WalletPrimary,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = expense.merchant,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "${userProfile.currencySymbol}${expense.amount.toInt()}",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = WalletPrimary
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider(color = WalletDivider)
                    Spacer(modifier = Modifier.height(20.dp))

                    DetailRow(label = "Title", value = expense.title)
                    DetailRow(label = "Category", value = expense.category.displayName)
                    DetailRow(label = "Date & Time", value = "${expense.date}, ${expense.time}")
                    DetailRow(label = "Payment Method", value = expense.paymentMethod)
                    if (expense.notes.isNotBlank()) {
                        DetailRow(label = "Notes", value = expense.notes)
                    }
                    DetailRow(label = "Transaction Status", value = "Completed", valueColor = WalletSuccess)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = "Share Receipt",
                onClick = {}
            )

            Spacer(modifier = Modifier.height(12.dp))

            SecondaryButton(
                text = "Download PDF Receipt",
                onClick = onBack
            )
        }
    }

    if (showEditSheet) {
        AddEditExpenseSheet(
            onDismiss = { showEditSheet = false },
            onSaveExpense = { updated ->
                repository.updateExpense(updated)
                showEditSheet = false
            },
            initialExpense = expense
        )
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = valueColor)
    }
}
