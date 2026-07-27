package com.walletwise.app.core.model

data class WalletScore(
    val score: Int, // e.g. 785
    val maxScore: Int = 900,
    val ratingLabel: String, // e.g. "Excellent", "Good", "Needs Attention"
    val changeThisMonth: Int, // e.g. +14
    val summaryMessage: String
)

data class Insight(
    val id: String,
    val title: String,
    val description: String,
    val tag: String, // e.g., "AI Suggestion", "Alert", "Tip"
    val actionText: String? = null
)

data class UserProfile(
    val id: String = "usr_101",
    val name: String = "Alex Morgan",
    val email: String = "alex.morgan@walletwise.io",
    val phone: String = "+91 98765 43210",
    val monthlyIncome: Double = 85000.0,
    val occupation: String = "Product Designer",
    val isPremium: Boolean = true,
    val avatarUrl: String? = null,
    val currencySymbol: String = "₹",
    val language: String = "English",
    val themeMode: String = "Light",
    val biometricEnabled: Boolean = true,
    val notificationsEnabled: Boolean = true
) {
    val completionPercentage: Int
        get() {
            var score = 0
            if (name.isNotBlank()) score += 20
            if (email.isNotBlank()) score += 20
            if (phone.isNotBlank()) score += 20
            if (monthlyIncome > 0) score += 20
            if (occupation.isNotBlank()) score += 20
            return score
        }
}

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val groupTag: String = "Today", // "Today", "Yesterday", "Earlier"
    val isRead: Boolean = false,
    val type: String // e.g. "ALERT", "AI", "SYSTEM", "BUDGET", "REMINDER", "REPORT"
)
