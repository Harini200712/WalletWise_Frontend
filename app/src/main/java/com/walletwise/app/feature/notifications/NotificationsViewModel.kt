package com.walletwise.app.feature.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walletwise.app.core.model.NotificationItem
import com.walletwise.app.core.ui.UiState
import com.walletwise.app.data.repository.WalletRepository
import kotlinx.coroutines.flow.*

class NotificationsViewModel(
    private val repository: WalletRepository = WalletRepository()
) : ViewModel() {

    val selectedFilter = MutableStateFlow("All")

    private val _notificationsState = MutableStateFlow<UiState<List<NotificationItem>>>(UiState.Loading)
    val notificationsState: StateFlow<UiState<List<NotificationItem>>> = _notificationsState.asStateFlow()

    init {
        combine(repository.notifications, selectedFilter) { list, filter ->
            val filtered = when (filter) {
                "Budget" -> list.filter { it.type == "ALERT" || it.type == "BUDGET" }
                "Reminder" -> list.filter { it.type == "REMINDER" || it.type == "SYSTEM" }
                "AI" -> list.filter { it.type == "AI" }
                "Reports" -> list.filter { it.type == "REPORT" || it.title.contains("Report", ignoreCase = true) }
                else -> list
            }
            if (filtered.isEmpty()) UiState.Empty else UiState.Success(filtered)
        }.onEach { state ->
            _notificationsState.value = state
        }.launchIn(viewModelScope)
    }

    fun setFilter(filter: String) {
        selectedFilter.value = filter
    }

    fun toggleReadStatus(id: String) {
        repository.toggleNotificationRead(id)
    }

    fun deleteNotification(id: String) {
        repository.deleteNotification(id)
    }

    fun clearAll() {
        repository.clearAllNotifications()
    }
}
