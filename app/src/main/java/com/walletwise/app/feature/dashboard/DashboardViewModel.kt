package com.walletwise.app.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walletwise.app.core.model.*
import com.walletwise.app.data.repository.WalletRepository
import kotlinx.coroutines.flow.*

data class DashboardUiState(
    val user: UserProfile? = null,
    val score: WalletScore? = null,
    val totalExpenseMonth: Double = 31909.0,
    val totalBudgetMonth: Double = 48000.0,
    val savingsMonth: Double = 16091.0,
    val recentExpenses: List<Expense> = emptyList(),
    val insights: List<Insight> = emptyList(),
    val prediction: Prediction? = null,
    val isLoading: Boolean = false
)

class DashboardViewModel(
    private val repository: WalletRepository = WalletRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        combine(
            repository.user,
            repository.score,
            repository.expenses,
            repository.insights,
            repository.prediction
        ) { user, score, expenses, insights, prediction ->
            val totalExpense = expenses.sumOf { it.amount }
            DashboardUiState(
                user = user,
                score = score,
                totalExpenseMonth = totalExpense,
                totalBudgetMonth = 48000.0,
                savingsMonth = (48000.0 - totalExpense).coerceAtLeast(0.0),
                recentExpenses = expenses.take(4),
                insights = insights,
                prediction = prediction,
                isLoading = false
            )
        }.onEach { state ->
            _uiState.value = state
        }.launchIn(viewModelScope)
    }
}
