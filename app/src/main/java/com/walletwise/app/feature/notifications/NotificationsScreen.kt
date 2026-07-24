package com.walletwise.app.feature.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Notifications
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
import com.walletwise.app.core.designsystem.*
import com.walletwise.app.core.designsystem.components.WalletCard
import com.walletwise.app.core.model.NotificationItem

@Composable
fun NotificationsScreen(
    viewModel: NotificationsViewModel = NotificationsViewModel(),
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WalletBackground)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Rounded.ArrowBack, contentDescription = "Back", tint = WalletTextPrimary)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Smart Notifications",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = WalletTextPrimary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(uiState.notifications) { notif ->
                NotificationRow(
                    item = notif,
                    onClick = { viewModel.markAsRead(notif.id) }
                )
            }
        }
    }
}

@Composable
private fun NotificationRow(
    item: NotificationItem,
    onClick: () -> Unit
) {
    WalletCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = if (item.isRead) 2.dp else 5.dp,
        backgroundColor = if (item.isRead) WalletSurface else WalletPrimaryLight.copy(alpha = 0.4f)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        when (item.type) {
                            "ALERT" -> WalletError.copy(alpha = 0.2f)
                            "AI" -> WalletAccentOrange.copy(alpha = 0.2f)
                            else -> WalletPrimaryLight
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (item.type) {
                        "ALERT" -> Icons.Rounded.Warning
                        "AI" -> Icons.Rounded.AutoAwesome
                        else -> Icons.Rounded.Notifications
                    },
                    contentDescription = item.type,
                    tint = when (item.type) {
                        "ALERT" -> WalletError
                        "AI" -> WalletAccentOrange
                        else -> WalletPrimary
                    },
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = WalletTextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.message,
                    fontSize = 13.sp,
                    color = WalletTextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = item.timestamp,
                    fontSize = 11.sp,
                    color = WalletTextSecondary
                )
            }
        }
    }
}
