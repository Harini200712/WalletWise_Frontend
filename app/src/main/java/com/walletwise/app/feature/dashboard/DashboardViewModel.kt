package com.walletwise.app.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walletwise.app.core.model.*
import com.walletwise.app.data.repository.WalletRepository
import kotlinx.coroutines.flow.*

data class DashboardUiState(
    val user: UserProfile? = null,
    val score: WalletScore? = null,
    val totalExpenseMonth: Double = 0.0,
    val totalBudgetMonth: Double = 0.0,
    val savingsMonth: Double = 0.0,
    val currencySymbol: String = "₹",
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
            repository.budgets,
            repository.insights,
            repository.prediction
        ) { user, score, expenses, budgets, insights, prediction ->
            val totalExpense = expenses.sumOf { it.amount }
            val totalBudget = budgets.sumOf { it.allocatedAmount }
            val savings = (user.monthlyIncome - totalExpense).coerceAtLeast(0.0)

            DashboardUiState(
                user = user,
                score = score,
                totalExpenseMonth = totalExpense,
                totalBudgetMonth = totalBudget,
                savingsMonth = savings,
                currencySymbol = user.currencySymbol,
                recentExpenses = expenses.take(5),
                insights = insights,
                prediction = prediction,
                isLoading = false
            )
        }.onEach { state ->
            _uiState.value = state
        }.launchIn(viewModelScope)
    }
}
