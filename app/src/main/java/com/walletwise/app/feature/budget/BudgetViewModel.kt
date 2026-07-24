package com.walletwise.app.feature.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walletwise.app.core.model.Budget
import com.walletwise.app.data.repository.WalletRepository
import kotlinx.coroutines.flow.*

data class BudgetUiState(
    val budgets: List<Budget> = emptyList(),
    val totalAllocated: Double = 0.0,
    val totalSpent: Double = 0.0,
    val overallPercentage: Float = 0f,
    val overspentCount: Int = 0
)

class BudgetViewModel(
    private val repository: WalletRepository = WalletRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(BudgetUiState())
    val uiState: StateFlow<BudgetUiState> = _uiState.asStateFlow()

    init {
        repository.budgets.onEach { list ->
            val totalAllocated = list.sumOf { it.allocatedAmount }
            val totalSpent = list.sumOf { it.spentAmount }
            val pct = if (totalAllocated > 0) (totalSpent / totalAllocated).toFloat() else 0f
            val overspent = list.count { it.isOverspent }

            _uiState.value = BudgetUiState(
                budgets = list,
                totalAllocated = totalAllocated,
                totalSpent = totalSpent,
                overallPercentage = pct,
                overspentCount = overspent
            )
        }.launchIn(viewModelScope)
    }

    fun addBudget(budget: Budget) {
        repository.addBudget(budget)
    }
}
