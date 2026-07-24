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
    val id: String,
    val name: String,
    val email: String,
    val isPremium: Boolean = true,
    val avatarUrl: String? = null,
    val currencySymbol: String = "₹",
    val themeMode: String = "Light",
    val biometricEnabled: Boolean = true
)

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val groupTag: String = "Today", // "Today", "Yesterday", "Earlier"
    val isRead: Boolean = false,
    val type: String // e.g. "ALERT", "AI", "SYSTEM"
)
