package com.walletwise.app.core.model

data class Prediction(
    val forecastedSpending: Double,
    val expectedSavings: Double,
    val confidenceScore: Int, // e.g. 94%
    val aiRecommendations: List<String>,
    val predictedCategoryBreakdown: Map<String, Double>
)
