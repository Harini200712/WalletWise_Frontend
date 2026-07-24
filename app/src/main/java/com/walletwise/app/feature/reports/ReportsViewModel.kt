package com.walletwise.app.feature.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walletwise.app.core.model.Expense
import com.walletwise.app.core.model.TimePeriod
import com.walletwise.app.data.repository.WalletRepository
import kotlinx.coroutines.flow.*

data class ReportsUiState(
    val selectedPeriod: TimePeriod = TimePeriod.MONTHLY,
    val monthlyCategoryData: Map<String, Float> = emptyMap(),
    val totalIncome: Double = 85000.0,
    val totalExpenses: Double = 0.0,
    val netSavings: Double = 0.0,
    val savingsRate: Int = 0,
    val topExpenseCategory: String = "Food & Dining"
)

class ReportsViewModel(
    private val repository: WalletRepository = WalletRepository()
) : ViewModel() {

    private val _selectedPeriod = MutableStateFlow(TimePeriod.MONTHLY)
    private val _uiState = MutableStateFlow(ReportsUiState())
    val uiState: StateFlow<ReportsUiState> = _uiState.asStateFlow()

    init {
        combine(repository.expenses, _selectedPeriod) { expenses, period ->
            val totalExp = expenses.sumOf { it.amount }
            val income = 85000.0
            val savings = (income - totalExp).coerceAtLeast(0.0)
            val rate = if (income > 0) ((savings / income) * 100).toInt() else 0

            val categoryGroup = expenses.groupBy { it.category.displayName }
                .mapValues { entry -> entry.value.sumOf { it.amount }.toFloat() }

            val maxCategoryAmount = categoryGroup.values.maxOrNull() ?: 1f
            val normalizedChartData = categoryGroup.mapValues { it.value / maxCategoryAmount }

            val topCat = categoryGroup.maxByOrNull { it.value }?.key ?: "Food & Dining"

            ReportsUiState(
                selectedPeriod = period,
                monthlyCategoryData = normalizedChartData,
                totalIncome = income,
                totalExpenses = totalExp,
                netSavings = savings,
                savingsRate = rate,
                topExpenseCategory = topCat
            )
        }.onEach { state ->
            _uiState.value = state
        }.launchIn(viewModelScope)
    }

    fun onPeriodSelected(period: TimePeriod) {
        _selectedPeriod.value = period
    }
}
