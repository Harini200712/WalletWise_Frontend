package com.walletwise.app.feature.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walletwise.app.core.model.NotificationItem
import com.walletwise.app.data.repository.WalletRepository
import kotlinx.coroutines.flow.*

data class NotificationsUiState(
    val notifications: List<NotificationItem> = emptyList()
)

class NotificationsViewModel(
    private val repository: WalletRepository = WalletRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    init {
        repository.notifications.onEach { list ->
            _uiState.value = NotificationsUiState(notifications = list)
        }.launchIn(viewModelScope)
    }

    fun markAsRead(id: String) {
        repository.markNotificationRead(id)
    }
}
