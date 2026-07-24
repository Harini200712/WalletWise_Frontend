package com.walletwise.app.data.repository

import com.walletwise.app.core.model.*
import com.walletwise.app.data.engine.WalletScoreEngine
import com.walletwise.app.data.mock.MockData
import kotlinx.coroutines.flow.*

class WalletRepository {

    private val _expenses = MutableStateFlow(MockData.sampleExpenses)
    val expenses: Flow<List<Expense>> = _expenses.asStateFlow()

    private val _budgets = MutableStateFlow(MockData.sampleBudgets)
    val budgets: Flow<List<Budget>> = _budgets.asStateFlow()

    private val _user = MutableStateFlow(MockData.sampleUser)
    val user: Flow<UserProfile> = _user.asStateFlow()

    private val _notifications = MutableStateFlow(MockData.sampleNotifications)
    val notifications: Flow<List<NotificationItem>> = _notifications.asStateFlow()

    // Dynamically computed score flow
    val score: Flow<WalletScore> = combine(_expenses, _budgets) { expList, bgtList ->
        WalletScoreEngine.calculateScore(expList, bgtList)
    }

    // Dynamically computed prediction flow
    val prediction: Flow<Prediction> = _expenses.map { expList ->
        WalletScoreEngine.generatePrediction(expList)
    }

    val insights: Flow<List<Insight>> = _expenses.map { expList ->
        val foodTotal = expList.filter { it.category == ExpenseCategory.FOOD }.sumOf { it.amount }
        listOf(
            Insight(
                id = "ins_1",
                title = "Weekend Spend Surge Detected",
                description = "Food & dining spending has reached ₹${foodTotal.toInt()}. Cooking at home twice a week can save ₹3,200 monthly.",
                tag = "AI Suggestion",
                actionText = "Set Dining Cap"
            ),
            Insight(
                id = "ins_2",
                title = "Unused Streaming Subscription",
                description = "Netflix Premium hasn't been accessed in 28 days. Consider pausing to optimize recurring bills.",
                tag = "Smart Alert",
                actionText = "Manage Subscriptions"
            )
        )
    }

    fun addExpense(expense: Expense) {
        _expenses.update { current -> listOf(expense) + current }
        updateBudgetsForExpense(expense.category, expense.amount)
        checkBudgetThresholdAlerts(expense.category)
    }

    fun updateExpense(updatedExpense: Expense) {
        _expenses.update { current ->
            current.map { if (it.id == updatedExpense.id) updatedExpense else it }
        }
    }

    fun deleteExpense(id: String) {
        _expenses.update { current -> current.filterNot { it.id == id } }
    }

    fun duplicateExpense(id: String) {
        val original = _expenses.value.firstOrNull { it.id == id }
        if (original != null) {
            val duplicated = original.copy(
                id = "exp_${System.currentTimeMillis()}",
                title = "${original.title} (Copy)",
                date = "Today",
                time = "Just now",
                timestamp = System.currentTimeMillis()
            )
            addExpense(duplicated)
        }
    }

    fun addBudget(budget: Budget) {
        _budgets.update { current -> listOf(budget) + current }
    }

    fun updateBudget(updatedBudget: Budget) {
        _budgets.update { current ->
            current.map { if (it.id == updatedBudget.id) updatedBudget else it }
        }
    }

    fun deleteBudget(id: String) {
        _budgets.update { current -> current.filterNot { it.id == id } }
    }

    fun updateUserProfile(updatedUser: UserProfile) {
        _user.value = updatedUser
    }

    fun markNotificationRead(id: String) {
        _notifications.update { current ->
            current.map { if (it.id == id) it.copy(isRead = true) else it }
        }
    }

    private fun updateBudgetsForExpense(category: ExpenseCategory, amount: Double) {
        _budgets.update { current ->
            current.map { bgt ->
                if (bgt.category == category) {
                    bgt.copy(spentAmount = bgt.spentAmount + amount)
                } else bgt
            }
        }
    }

    private fun checkBudgetThresholdAlerts(category: ExpenseCategory) {
        val targetBudget = _budgets.value.firstOrNull { it.category == category } ?: return
        val ratio = targetBudget.spentAmount / targetBudget.allocatedAmount
        if (ratio >= 0.85) {
            val newAlert = NotificationItem(
                id = "n_${System.currentTimeMillis()}",
                title = "${category.displayName} Budget Alert",
                message = "${category.displayName} is at ${(ratio * 100).toInt()}% capacity (₹${targetBudget.spentAmount.toInt()}/₹${targetBudget.allocatedAmount.toInt()}).",
                timestamp = "Just now",
                groupTag = "Today",
                isRead = false,
                type = "ALERT"
            )
            _notifications.update { current -> listOf(newAlert) + current }
        }
    }
}
