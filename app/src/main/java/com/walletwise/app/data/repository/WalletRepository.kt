package com.walletwise.app.data.repository

import com.walletwise.app.core.model.*
import com.walletwise.app.data.engine.WalletScoreEngine
import com.walletwise.app.data.mock.MockData
import kotlinx.coroutines.flow.*

class WalletRepository {

    companion object {
        private val _sharedExpenses = MutableStateFlow(MockData.sampleExpenses)
        private val _sharedBudgets = MutableStateFlow(MockData.sampleBudgets)
        private val _sharedUser = MutableStateFlow(UserProfile())
        private val _sharedNotifications = MutableStateFlow(MockData.sampleNotifications)

        private var lastDeletedExpense: Expense? = null
    }

    val expenses: Flow<List<Expense>> = _sharedExpenses.asStateFlow()
    val budgets: Flow<List<Budget>> = _sharedBudgets.asStateFlow()
    val user: Flow<UserProfile> = _sharedUser.asStateFlow()
    val notifications: Flow<List<NotificationItem>> = _sharedNotifications.asStateFlow()

    // Dynamically computed score flow
    val score: Flow<WalletScore> = combine(_sharedExpenses, _sharedBudgets) { expList, bgtList ->
        WalletScoreEngine.calculateScore(expList, bgtList)
    }

    // Dynamically computed prediction flow
    val prediction: Flow<Prediction> = _sharedExpenses.map { expList ->
        WalletScoreEngine.generatePrediction(expList)
    }

    val insights: Flow<List<Insight>> = _sharedExpenses.map { expList ->
        val foodTotal = expList.filter { it.category == ExpenseCategory.FOOD }.sumOf { it.amount }
        val sym = _sharedUser.value.currencySymbol
        listOf(
            Insight(
                id = "ins_1",
                title = "Weekend Spend Surge Detected",
                description = "Food & dining spending has reached $sym${foodTotal.toInt()}. Cooking at home twice a week can save $sym3,200 monthly.",
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
        _sharedExpenses.update { current -> listOf(expense) + current }
        updateBudgetsForExpense(expense.category, expense.amount)
        checkBudgetThresholdAlerts(expense.category)

        // Push notification
        addNotification(
            title = "Expense Added",
            message = "Logged '${expense.title}' (${_sharedUser.value.currencySymbol}${expense.amount.toInt()}) in ${expense.category.displayName}.",
            type = "SYSTEM"
        )
    }

    fun updateExpense(updatedExpense: Expense) {
        _sharedExpenses.update { current ->
            current.map { if (it.id == updatedExpense.id) updatedExpense else it }
        }
        addNotification(
            title = "Expense Updated",
            message = "Updated details for '${updatedExpense.title}'.",
            type = "SYSTEM"
        )
    }

    fun deleteExpense(id: String) {
        val target = _sharedExpenses.value.firstOrNull { it.id == id }
        if (target != null) {
            lastDeletedExpense = target
            _sharedExpenses.update { current -> current.filterNot { it.id == id } }
            addNotification(
                title = "Expense Removed",
                message = "Deleted transaction '${target.title}'.",
                type = "ALERT"
            )
        }
    }

    fun undoLastDelete(): Boolean {
        val deleted = lastDeletedExpense
        if (deleted != null) {
            _sharedExpenses.update { current -> listOf(deleted) + current }
            lastDeletedExpense = null
            addNotification(
                title = "Expense Restored",
                message = "Restored transaction '${deleted.title}'.",
                type = "SYSTEM"
            )
            return true
        }
        return false
    }

    fun duplicateExpense(id: String) {
        val original = _sharedExpenses.value.firstOrNull { it.id == id }
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
        _sharedBudgets.update { current -> listOf(budget) + current }
        addNotification(
            title = "Budget Created",
            message = "Set ${_sharedUser.value.currencySymbol}${budget.allocatedAmount.toInt()} limit for ${budget.category.displayName}.",
            type = "BUDGET"
        )
    }

    fun updateBudget(updatedBudget: Budget) {
        _sharedBudgets.update { current ->
            current.map { if (it.id == updatedBudget.id) updatedBudget else it }
        }
        addNotification(
            title = "Budget Updated",
            message = "Adjusted ${updatedBudget.category.displayName} budget cap to ${_sharedUser.value.currencySymbol}${updatedBudget.allocatedAmount.toInt()}.",
            type = "BUDGET"
        )
    }

    fun deleteBudget(id: String) {
        val target = _sharedBudgets.value.firstOrNull { it.id == id }
        _sharedBudgets.update { current -> current.filterNot { it.id == id } }
        if (target != null) {
            addNotification(
                title = "Budget Removed",
                message = "Deleted budget allocation for ${target.category.displayName}.",
                type = "ALERT"
            )
        }
    }

    fun updateUserProfile(updatedUser: UserProfile) {
        _sharedUser.value = updatedUser
        addNotification(
            title = "Profile Updated",
            message = "Your profile information & settings were updated successfully.",
            type = "SYSTEM"
        )
    }

    fun updateThemeMode(mode: String) {
        _sharedUser.update { it.copy(themeMode = mode) }
    }

    fun updateLanguage(lang: String) {
        _sharedUser.update { it.copy(language = lang) }
    }

    fun updateCurrency(symbol: String) {
        _sharedUser.update { it.copy(currencySymbol = symbol) }
    }

    fun toggleBiometric(enabled: Boolean) {
        _sharedUser.update { it.copy(biometricEnabled = enabled) }
    }

    fun toggleNotifications(enabled: Boolean) {
        _sharedUser.update { it.copy(notificationsEnabled = enabled) }
    }

    fun markNotificationRead(id: String) {
        _sharedNotifications.update { current ->
            current.map { if (it.id == id) it.copy(isRead = true) else it }
        }
    }

    fun toggleNotificationRead(id: String) {
        _sharedNotifications.update { current ->
            current.map { if (it.id == id) it.copy(isRead = !it.isRead) else it }
        }
    }

    fun deleteNotification(id: String) {
        _sharedNotifications.update { current -> current.filterNot { it.id == id } }
    }

    fun clearAllNotifications() {
        _sharedNotifications.value = emptyList()
    }

    private fun addNotification(title: String, message: String, type: String) {
        val item = NotificationItem(
            id = "n_${System.currentTimeMillis()}",
            title = title,
            message = message,
            timestamp = "Just now",
            groupTag = "Today",
            isRead = false,
            type = type
        )
        _sharedNotifications.update { current -> listOf(item) + current }
    }

    private fun updateBudgetsForExpense(category: ExpenseCategory, amount: Double) {
        _sharedBudgets.update { current ->
            current.map { bgt ->
                if (bgt.category == category) {
                    bgt.copy(spentAmount = bgt.spentAmount + amount)
                } else bgt
            }
        }
    }

    private fun checkBudgetThresholdAlerts(category: ExpenseCategory) {
        val targetBudget = _sharedBudgets.value.firstOrNull { it.category == category } ?: return
        val ratio = targetBudget.spentAmount / targetBudget.allocatedAmount
        if (ratio >= 0.85) {
            val newAlert = NotificationItem(
                id = "n_${System.currentTimeMillis()}",
                title = "${category.displayName} Budget Alert",
                message = "${category.displayName} is at ${(ratio * 100).toInt()}% capacity (${_sharedUser.value.currencySymbol}${targetBudget.spentAmount.toInt()}/${_sharedUser.value.currencySymbol}${targetBudget.allocatedAmount.toInt()}).",
                timestamp = "Just now",
                groupTag = "Today",
                isRead = false,
                type = "ALERT"
            )
            _sharedNotifications.update { current -> listOf(newAlert) + current }
        }
    }
}
