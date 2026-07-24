package com.walletwise.app.core.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.walletwise.app.core.designsystem.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickActionsBottomSheet(
    onDismiss: () -> Unit,
    onAddExpense: () -> Unit,
    onScanReceipt: () -> Unit,
    onCreateBudget: () -> Unit,
    onAiAssistant: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = BottomSheetShape,
        containerColor = WalletSurface,
        scrimColor = Color.Black.copy(alpha = 0.4f)
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
                text = "Quick Actions",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = WalletTextPrimary
            )
            Text(
                text = "Choose an option to manage your finances instantly",
                fontSize = 13.sp,
                color = WalletTextSecondary
            )

            Spacer(modifier = Modifier.height(20.dp))

            QuickActionTile(
                title = "Add Expense",
                subtitle = "Record a manual transaction quickly",
                icon = Icons.Rounded.AddCircleOutline,
                iconBg = WalletPrimaryLight,
                iconTint = WalletPrimary,
                onClick = {
                    onDismiss()
                    onAddExpense()
                }
            )

            QuickActionTile(
                title = "Scan Receipt (OCR)",
                subtitle = "Auto-extract merchant & total using Google ML Kit",
                icon = Icons.Rounded.QrCodeScanner,
                iconBg = Color(0xFFFFF3E0),
                iconTint = WalletAccentOrange,
                onClick = {
                    onDismiss()
                    onScanReceipt()
                }
            )

            QuickActionTile(
                title = "Create Budget",
                subtitle = "Set category limits & prevent overspending",
                icon = Icons.Rounded.AccountBalanceWallet,
                iconBg = Color(0xFFE8F5E9),
                iconTint = WalletSuccess,
                onClick = {
                    onDismiss()
                    onCreateBudget()
                }
            )

            QuickActionTile(
                title = "AI Financial Assistant",
                subtitle = "Ask Gemini AI about your spending habits & tips",
                icon = Icons.Rounded.AutoAwesome,
                iconBg = Color(0xFFFFEBEE),
                iconTint = WalletAccentCoral,
                onClick = {
                    onDismiss()
                    onAiAssistant()
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun QuickActionTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clip(TextFieldShape)
            .clickable { onClick() }
            .background(WalletBackground)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = WalletTextPrimary
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = WalletTextSecondary
            )
        }

        Icon(
            imageVector = Icons.Rounded.ChevronRight,
            contentDescription = "Go",
            tint = WalletTextSecondary
        )
    }
}
