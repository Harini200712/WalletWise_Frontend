package com.walletwise.app.core.model

data class Expense(
    val id: String,
    val title: String,
    val merchant: String,
    val amount: Double,
    val category: ExpenseCategory,
    val date: String,
    val time: String = "12:00 PM",
    val paymentMethod: String = "UPI / Card",
    val notes: String = "",
    val receiptUri: String? = null,
    val isRecurring: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

enum class ExpenseCategory(val displayName: String, val iconName: String) {
    FOOD("Food & Dining", "Restaurant"),
    SHOPPING("Shopping", "ShoppingBag"),
    BILLS("Bills & Utilities", "Receipt"),
    TRANSPORT("Transport", "DirectionsCar"),
    HEALTH("Health & Wellness", "LocalHospital"),
    ENTERTAINMENT("Entertainment", "Movie"),
    TRAVEL("Travel & Vacation", "Flight"),
    OTHER("Others", "Category")
}

enum class ExpenseSortOption(val displayName: String) {
    NEWEST("Newest First"),
    OLDEST("Oldest First"),
    HIGHEST_AMOUNT("Highest Amount"),
    LOWEST_AMOUNT("Lowest Amount"),
    CATEGORY("Category"),
    MERCHANT("Merchant")
}

enum class TimePeriod(val displayName: String) {
    DAILY("Daily"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    YEARLY("Yearly")
}
