package com.walletwise.app.feature.budget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.walletwise.app.core.designsystem.components.WalletTextField
import com.walletwise.app.core.model.Budget
import com.walletwise.app.core.model.ExpenseCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditBudgetSheet(
    onDismiss: () -> Unit,
    onSaveBudget: (Budget) -> Unit,
    onDeleteBudget: ((String) -> Unit)? = null,
    initialBudget: Budget? = null
) {
    var amount by remember { mutableStateOf(initialBudget?.allocatedAmount?.toInt()?.toString() ?: "") }
    var selectedCategory by remember { mutableStateOf(initialBudget?.category ?: ExpenseCategory.SHOPPING) }
    var amountError by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = BottomSheetShape,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
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
                text = if (initialBudget == null) "Set Monthly Category Budget" else "Edit Budget Limit",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("Category", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(ExpenseCategory.values()) { cat ->
                    val isSelected = cat == selectedCategory
                    Box(
                        modifier = Modifier
                            .clip(ChipShape)
                            .background(if (isSelected) WalletPrimary else MaterialTheme.colorScheme.background)
                            .clickable { selectedCategory = cat }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = cat.displayName,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            WalletTextField(
                value = amount,
                onValueChange = {
                    amount = it
                    amountError = null
                },
                label = "Allocated Monthly Cap",
                placeholder = "e.g. 10000",
                errorMessage = amountError
            )

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = if (initialBudget == null) "Save Budget" else "Update Budget",
                onClick = {
                    val cap = amount.toDoubleOrNull()
                    if (cap == null || cap <= 0) {
                        amountError = "Enter a valid budget amount"
                    } else {
                        val budget = Budget(
                            id = initialBudget?.id ?: "bgt_${System.currentTimeMillis()}",
                            category = selectedCategory,
                            allocatedAmount = cap,
                            spentAmount = initialBudget?.spentAmount ?: 0.0
                        )
                        onSaveBudget(budget)
                        onDismiss()
                    }
                }
            )

            if (initialBudget != null && onDeleteBudget != null) {
                Spacer(modifier = Modifier.height(12.dp))
                SecondaryButton(
                    text = "Delete Budget Category",
                    onClick = {
                        onDeleteBudget(initialBudget.id)
                        onDismiss()
                    },
                    containerColor = Color(0xFFFFF0F0),
                    contentColor = WalletError
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
