package com.walletwise.app.feature.expenses

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AttachFile
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
import com.walletwise.app.core.designsystem.components.WalletTextField
import com.walletwise.app.core.model.Expense
import com.walletwise.app.core.model.ExpenseCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditExpenseSheet(
    onDismiss: () -> Unit,
    onSaveExpense: (Expense) -> Unit,
    initialExpense: Expense? = null
) {
    var title by remember { mutableStateOf(initialExpense?.title ?: "") }
    var merchant by remember { mutableStateOf(initialExpense?.merchant ?: "") }
    var amount by remember { mutableStateOf(initialExpense?.amount?.let { if (it > 0) it.toString() else "" } ?: "") }
    var selectedCategory by remember { mutableStateOf(initialExpense?.category ?: ExpenseCategory.FOOD) }
    var paymentMethod by remember { mutableStateOf(initialExpense?.paymentMethod ?: "UPI") }
    var notes by remember { mutableStateOf(initialExpense?.notes ?: "") }
    var hasReceipt by remember { mutableStateOf(initialExpense?.receiptUri != null) }

    var titleError by remember { mutableStateOf<String?>(null) }
    var amountError by remember { mutableStateOf<String?>(null) }

    val paymentMethods = listOf("UPI", "Credit Card", "Debit Card", "Cash", "NetBanking")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = BottomSheetShape,
        containerColor = WalletSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(5.dp)
                    .clip(CircleShape)
                    .background(WalletDivider)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (initialExpense == null) "Add New Expense" else "Edit Expense",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = WalletTextPrimary
            )

            Spacer(modifier = Modifier.height(20.dp))

            WalletTextField(
                value = title,
                onValueChange = {
                    title = it
                    titleError = null
                },
                label = "Title *",
                placeholder = "e.g. Grocery Shopping",
                isError = titleError != null,
                errorMessage = titleError
            )

            Spacer(modifier = Modifier.height(14.dp))

            WalletTextField(
                value = amount,
                onValueChange = {
                    amount = it
                    amountError = null
                },
                label = "Amount (₹) *",
                placeholder = "0.00",
                isError = amountError != null,
                errorMessage = amountError
            )

            Spacer(modifier = Modifier.height(14.dp))

            WalletTextField(
                value = merchant,
                onValueChange = { merchant = it },
                label = "Merchant / Store Name",
                placeholder = "e.g. Walmart, Swiggy"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Category Selection Row
            Text("Category", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = WalletTextPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(ExpenseCategory.values()) { cat ->
                    val isSelected = cat == selectedCategory
                    Box(
                        modifier = Modifier
                            .clip(ChipShape)
                            .background(if (isSelected) WalletPrimary else WalletBackground)
                            .clickable { selectedCategory = cat }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = cat.displayName,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                            color = if (isSelected) Color.White else WalletTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Payment Method Selection Row
            Text("Payment Method", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = WalletTextPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(paymentMethods) { pm ->
                    val isSelected = pm == paymentMethod
                    Box(
                        modifier = Modifier
                            .clip(ChipShape)
                            .background(if (isSelected) WalletAccentOrange else WalletBackground)
                            .clickable { paymentMethod = pm }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = pm,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                            color = if (isSelected) Color.White else WalletTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            WalletTextField(
                value = notes,
                onValueChange = { notes = it },
                label = "Notes / Description",
                placeholder = "Optional notes about this expense"
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Receipt Attachment Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(TextFieldShape)
                    .background(WalletBackground)
                    .clickable { hasReceipt = !hasReceipt }
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.AttachFile, contentDescription = "Attach", tint = WalletPrimary)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (hasReceipt) "Receipt Attached (receipt_scanned.jpg)" else "Attach Receipt Photo (OCR)",
                        fontSize = 13.sp,
                        color = if (hasReceipt) WalletSuccess else WalletTextSecondary,
                        fontWeight = if (hasReceipt) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = if (initialExpense == null) "Save Expense" else "Update Expense",
                onClick = {
                    var isValid = true
                    if (title.isBlank()) {
                        titleError = "Title is required"
                        isValid = false
                    }
                    val amountValue = amount.toDoubleOrNull()
                    if (amountValue == null || amountValue <= 0) {
                        amountError = "Enter a valid positive amount"
                        isValid = false
                    }

                    if (isValid) {
                        val newExp = Expense(
                            id = initialExpense?.id ?: "exp_${System.currentTimeMillis()}",
                            title = title,
                            merchant = merchant.ifBlank { "Store" },
                            amount = amountValue!!,
                            category = selectedCategory,
                            date = "Today",
                            time = "Just now",
                            paymentMethod = paymentMethod,
                            notes = notes,
                            receiptUri = if (hasReceipt) "content://receipt/sample.jpg" else null
                        )
                        onSaveExpense(newExp)
                        onDismiss()
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
