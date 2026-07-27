package com.walletwise.app.feature.notifications

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
import com.walletwise.app.core.designsystem.components.ConfirmationDialog
import com.walletwise.app.core.designsystem.components.EmptyState
import com.walletwise.app.core.designsystem.components.LoadingView
import com.walletwise.app.core.designsystem.components.WalletCard
import com.walletwise.app.core.designsystem.components.WalletChips
import com.walletwise.app.core.model.NotificationItem
import com.walletwise.app.core.ui.UiState

@Composable
fun NotificationsScreen(
    viewModel: NotificationsViewModel = viewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.notificationsState.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()

    var showClearAllConfirm by remember { mutableStateOf(false) }

    val filterOptions = listOf("All", "Budget", "Reminder", "AI", "Reports")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
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
                    text = "Notification Center",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            if (state is UiState.Success) {
                TextButton(onClick = { showClearAllConfirm = true }) {
                    Text(
                        text = "Clear All",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        // Filter Chips Bar
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filterOptions) { filter ->
                WalletChips(
                    label = filter,
                    isSelected = filter == selectedFilter,
                    onClick = { viewModel.setFilter(filter) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Content
        when (state) {
            is UiState.Loading -> {
                LoadingView(message = "Fetching notifications...")
            }
            is UiState.Empty -> {
                EmptyState(
                    title = "No Notifications",
                    description = "You're all caught up! There are no $selectedFilter notifications right now.",
                    icon = Icons.Rounded.NotificationsNone
                )
            }
            is UiState.Error -> {
                EmptyState(
                    title = "Error",
                    description = (state as UiState.Error).message
                )
            }
            is UiState.Success -> {
                val notifications = (state as UiState.Success<List<NotificationItem>>).data
                val grouped = notifications.groupBy { it.groupTag }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    listOf("Today", "Yesterday", "Earlier").forEach { groupKey ->
                        val groupItems = grouped[groupKey]
                        if (!groupItems.isNullOrEmpty()) {
                            item {
                                Text(
                                    text = groupKey,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = WalletPrimary,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                            items(groupItems, key = { it.id }) { notif ->
                                NotificationRow(
                                    item = notif,
                                    onToggleRead = { viewModel.toggleReadStatus(notif.id) },
                                    onDelete = { viewModel.deleteNotification(notif.id) }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }

    ConfirmationDialog(
        showDialog = showClearAllConfirm,
        onDismiss = { showClearAllConfirm = false },
        onConfirm = { viewModel.clearAll() },
        title = "Clear Notifications",
        message = "Are you sure you want to delete all notifications?",
        confirmText = "Clear All",
        isDestructive = true
    )
}

@Composable
private fun NotificationRow(
    item: NotificationItem,
    onToggleRead: () -> Unit,
    onDelete: () -> Unit
) {
    WalletCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = if (item.isRead) 2.dp else 5.dp,
        backgroundColor = if (item.isRead) MaterialTheme.colorScheme.surface else WalletPrimaryLight.copy(alpha = 0.35f)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(
                        when (item.type) {
                            "ALERT", "BUDGET" -> WalletError.copy(alpha = 0.15f)
                            "AI" -> WalletAccentOrange.copy(alpha = 0.15f)
                            else -> WalletPrimaryLight
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (item.type) {
                        "ALERT", "BUDGET" -> Icons.Rounded.Warning
                        "AI" -> Icons.Rounded.AutoAwesome
                        "REPORT" -> Icons.Rounded.Assessment
                        else -> Icons.Rounded.Notifications
                    },
                    contentDescription = item.type,
                    tint = when (item.type) {
                        "ALERT", "BUDGET" -> WalletError
                        "AI" -> WalletAccentOrange
                        else -> WalletPrimary
                    },
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                        Icon(
                            imageVector = Icons.Rounded.DeleteOutline,
                            contentDescription = "Delete",
                            tint = WalletTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.message,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.timestamp,
                        fontSize = 11.sp,
                        color = WalletTextSecondary
                    )
                    Text(
                        text = if (item.isRead) "Mark Unread" else "Mark Read",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = WalletPrimary,
                        modifier = Modifier.clickable { onToggleRead() }
                    )
                }
            }
        }
    }
}
