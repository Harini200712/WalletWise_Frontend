package com.walletwise.app.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walletwise.app.core.model.UserProfile
import com.walletwise.app.data.repository.WalletRepository
import kotlinx.coroutines.flow.*

data class ProfileUiState(
    val user: UserProfile? = null
)

class ProfileViewModel(
    private val repository: WalletRepository = WalletRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        repository.user.onEach { u ->
            _uiState.value = ProfileUiState(user = u)
        }.launchIn(viewModelScope)
    }

    fun updateUserProfile(updatedUser: UserProfile) {
        repository.updateUserProfile(updatedUser)
    }
}
