package com.walletwise.app.feature.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walletwise.app.core.model.Expense
import com.walletwise.app.core.model.ExpenseCategory
import com.walletwise.app.core.model.ExpenseSortOption
import com.walletwise.app.data.repository.WalletRepository
import kotlinx.coroutines.flow.*

data class ExpensesUiState(
    val expenses: List<Expense> = emptyList(),
    val filteredExpenses: List<Expense> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: String = "All",
    val sortOption: ExpenseSortOption = ExpenseSortOption.NEWEST,
    val categories: List<String> = listOf("All", "Food & Dining", "Shopping", "Bills & Utilities", "Transport", "Health & Wellness", "Entertainment", "Travel & Vacation", "Others"),
    val isLoading: Boolean = false
)

class ExpensesViewModel(
    private val repository: WalletRepository = WalletRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpensesUiState())
    val uiState: StateFlow<ExpensesUiState> = _uiState.asStateFlow()

    init {
        repository.expenses.onEach { list ->
            _uiState.update { state ->
                state.copy(
                    expenses = list,
                    filteredExpenses = processExpenses(list, state.searchQuery, state.selectedCategory, state.sortOption)
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { state ->
            state.copy(
                searchQuery = query,
                filteredExpenses = processExpenses(state.expenses, query, state.selectedCategory, state.sortOption)
            )
        }
    }

    fun onCategorySelected(category: String) {
        _uiState.update { state ->
            state.copy(
                selectedCategory = category,
                filteredExpenses = processExpenses(state.expenses, state.searchQuery, category, state.sortOption)
            )
        }
    }

    fun onSortOptionSelected(sortOption: ExpenseSortOption) {
        _uiState.update { state ->
            state.copy(
                sortOption = sortOption,
                filteredExpenses = processExpenses(state.expenses, state.searchQuery, state.selectedCategory, sortOption)
            )
        }
    }

    fun deleteExpense(id: String) {
        repository.deleteExpense(id)
    }

    fun undoLastDelete() {
        repository.undoLastDelete()
    }

    fun duplicateExpense(id: String) {
        repository.duplicateExpense(id)
    }

    fun addExpense(expense: Expense) {
        repository.addExpense(expense)
    }

    fun updateExpense(expense: Expense) {
        repository.updateExpense(expense)
    }

    private fun processExpenses(
        list: List<Expense>,
        query: String,
        category: String,
        sort: ExpenseSortOption
    ): List<Expense> {
        val filtered = list.filter { item ->
            val matchesQuery = item.title.contains(query, ignoreCase = true) ||
                    item.merchant.contains(query, ignoreCase = true) ||
                    item.notes.contains(query, ignoreCase = true) ||
                    item.amount.toString().contains(query)
            val matchesCategory = if (category == "All") true else item.category.displayName.equals(category, ignoreCase = true)
            matchesQuery && matchesCategory
        }

        return when (sort) {
            ExpenseSortOption.NEWEST -> filtered.sortedByDescending { it.timestamp }
            ExpenseSortOption.OLDEST -> filtered.sortedBy { it.timestamp }
            ExpenseSortOption.HIGHEST_AMOUNT -> filtered.sortedByDescending { it.amount }
            ExpenseSortOption.LOWEST_AMOUNT -> filtered.sortedBy { it.amount }
            ExpenseSortOption.CATEGORY -> filtered.sortedBy { it.category.displayName }
            ExpenseSortOption.MERCHANT -> filtered.sortedBy { it.merchant }
        }
    }
}
