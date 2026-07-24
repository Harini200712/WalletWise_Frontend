package com.walletwise.app.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")
    
    object Dashboard : Screen("dashboard")
    object Expenses : Screen("expenses")
    object ExpenseDetail : Screen("expense_detail/{expenseId}") {
        fun createRoute(id: String) = "expense_detail/$id"
    }
    object Budget : Screen("budget")
    object Reports : Screen("reports")
    object Prediction : Screen("prediction")
    object Notifications : Screen("notifications")
    object Profile : Screen("profile")
}
