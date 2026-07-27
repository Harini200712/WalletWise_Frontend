package com.walletwise.app.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walletwise.app.core.model.UserProfile
import com.walletwise.app.core.ui.UiState
import com.walletwise.app.data.repository.WalletRepository
import kotlinx.coroutines.flow.*

class ProfileViewModel(
    private val repository: WalletRepository = WalletRepository()
) : ViewModel() {

    private val _userState = MutableStateFlow<UiState<UserProfile>>(UiState.Loading)
    val userState: StateFlow<UiState<UserProfile>> = _userState.asStateFlow()

    val rawUser: StateFlow<UserProfile> = repository.user
        .stateIn(viewModelScope, SharingStarted.Eagerly, UserProfile())

    init {
        repository.user.onEach { user ->
            _userState.value = UiState.Success(user)
        }.launchIn(viewModelScope)
    }

    fun saveProfile(
        name: String,
        email: String,
        phone: String,
        income: Double,
        occupation: String
    ) {
        val current = rawUser.value
        val updated = current.copy(
            name = name,
            email = email,
            phone = phone,
            monthlyIncome = income,
            occupation = occupation
        )
        repository.updateUserProfile(updated)
    }

    fun setThemeMode(mode: String) {
        repository.updateThemeMode(mode)
    }

    fun setLanguage(lang: String) {
        repository.updateLanguage(lang)
    }

    fun setCurrency(symbol: String) {
        repository.updateCurrency(symbol)
    }

    fun toggleBiometrics(enabled: Boolean) {
        repository.toggleBiometric(enabled)
    }

    fun toggleNotifications(enabled: Boolean) {
        repository.toggleNotifications(enabled)
    }
}
