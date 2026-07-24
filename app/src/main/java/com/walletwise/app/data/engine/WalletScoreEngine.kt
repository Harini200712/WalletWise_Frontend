package com.walletwise.app.data.engine

import com.walletwise.app.core.model.*
import kotlin.math.roundToInt

object WalletScoreEngine {

    const val DEFAULT_MONTHLY_INCOME = 85000.0

    fun calculateScore(expenses: List<Expense>, budgets: List<Budget>): WalletScore {
        val totalSpent = expenses.sumOf { it.amount }
        val income = DEFAULT_MONTHLY_INCOME
        val savingsRate = ((income - totalSpent) / income).coerceIn(-0.5, 1.0)

        val totalBudgeted = budgets.sumOf { it.allocatedAmount }
        val budgetAdherenceRatio = if (totalBudgeted > 0) {
            (1.0 - (totalSpent / totalBudgeted)).coerceIn(-0.5, 1.0)
        } else 0.2

        // Base score starts at 700
        var scoreVal = 700 + (savingsRate * 150) + (budgetAdherenceRatio * 50)
        scoreVal = scoreVal.coerceIn(300.0, 900.0)

        val finalScore = scoreVal.roundToInt()

        val (rating, message) = when {
            finalScore >= 800 -> "Excellent" to "Top 5% financial discipline! Exceptional savings & budget adherence."
            finalScore >= 720 -> "Good" to "Healthy financial control. Lower impulse shopping observed this month."
            finalScore >= 640 -> "Fair" to "Moderate spend load. Keep dining & shopping categories capped."
            else -> "Needs Attention" to "High spending velocity detected. Review category budgets immediately."
        }

        return WalletScore(
            score = finalScore,
            maxScore = 900,
            ratingLabel = rating,
            changeThisMonth = if (finalScore >= 750) 14 else -8,
            summaryMessage = message
        )
    }

    fun generatePrediction(expenses: List<Expense>): Prediction {
        val totalSpent = expenses.sumOf { it.amount }
        val runRateMultiplier = 1.08
        val forecasted = totalSpent * runRateMultiplier
        val income = DEFAULT_MONTHLY_INCOME
        val expectedSavings = (income - forecasted).coerceAtLeast(0.0)

        val confidence = when {
            expenses.size > 20 -> 94
            expenses.size > 10 -> 88
            else -> 78
        }

        // Category breakdown calculation
        val categoryTotals = expenses.groupBy { it.category.displayName }
            .mapValues { entry -> entry.value.sumOf { it.amount } }

        val foodSpent = categoryTotals["Food & Dining"] ?: 0.0
        val shoppingSpent = categoryTotals["Shopping"] ?: 0.0

        val recommendations = mutableListOf<String>()

        if (foodSpent > totalSpent * 0.20) {
            recommendations.add("Food expenses account for ${((foodSpent / totalSpent) * 100).toInt()}% of total spend. Reducing dining out could save ₹${(foodSpent * 0.25).toInt()} monthly.")
        }
        if (shoppingSpent > totalSpent * 0.15) {
            recommendations.add("Shopping velocity is high (₹${shoppingSpent.toInt()}). Consider setting a ₹7,500 monthly cap.")
        }
        recommendations.add("Shift ₹${(expectedSavings * 0.3).toInt()} into High-Yield Liquid Funds before July 28th to earn interest.")
        recommendations.add("Utility bills are forecasted to rise by 8% next month due to seasonal AC usage.")

        return Prediction(
            forecastedSpending = forecasted,
            expectedSavings = expectedSavings,
            confidenceScore = confidence,
            aiRecommendations = recommendations,
            predictedCategoryBreakdown = categoryTotals.mapValues { it.value * 1.05 }
        )
    }
}
