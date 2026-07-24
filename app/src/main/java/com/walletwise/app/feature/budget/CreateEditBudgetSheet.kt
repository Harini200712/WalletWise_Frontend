package com.walletwise.app.feature.budget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.walletwise.app.core.designsystem.*
import com.walletwise.app.core.designsystem.components.PrimaryButton
import com.walletwise.app.core.designsystem.components.WalletTextField
import com.walletwise.app.core.model.Budget
import com.walletwise.app.core.model.ExpenseCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditBudgetSheet(
    onDismiss: () -> Unit,
    onSaveBudget: (Budget) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(ExpenseCategory.SHOPPING) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = BottomSheetShape,
        containerColor = WalletSurface
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
                text = "Set Monthly Category Budget",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = WalletTextPrimary
            )

            Spacer(modifier = Modifier.height(20.dp))

            WalletTextField(
                value = amount,
                onValueChange = { amount = it },
                label = "Allocated Budget Cap (₹)",
                placeholder = "e.g. 10000"
            )

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = "Create Budget",
                onClick = {
                    val cap = amount.toDoubleOrNull() ?: 5000.0
                    val newBudget = Budget(
                        id = "bgt_${System.currentTimeMillis()}",
                        category = selectedCategory,
                        allocatedAmount = cap,
                        spentAmount = 0.0
                    )
                    onSaveBudget(newBudget)
                    onDismiss()
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
