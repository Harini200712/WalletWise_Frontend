package com.walletwise.app.core.model

data class Budget(
    val id: String,
    val category: ExpenseCategory,
    val allocatedAmount: Double,
    val spentAmount: Double,
    val month: String = "July 2026"
) {
    val remainingAmount: Double get() = allocatedAmount - spentAmount
    val progressPercentage: Float get() = (spentAmount / allocatedAmount).toFloat().coerceIn(0f, 1f)
    val isOverspent: Boolean get() = spentAmount > allocatedAmount
}
