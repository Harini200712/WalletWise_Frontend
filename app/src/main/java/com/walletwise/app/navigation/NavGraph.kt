package com.walletwise.app.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.walletwise.app.core.designsystem.WalletWiseTheme
import com.walletwise.app.core.designsystem.components.QuickActionsBottomSheet
import com.walletwise.app.core.designsystem.components.WalletBottomBar
import com.walletwise.app.data.repository.WalletRepository
import com.walletwise.app.feature.auth.*
import com.walletwise.app.feature.budget.BudgetScreen
import com.walletwise.app.feature.budget.CreateEditBudgetSheet
import com.walletwise.app.feature.dashboard.DashboardScreen
import com.walletwise.app.feature.expenses.AddEditExpenseSheet
import com.walletwise.app.feature.expenses.ExpenseDetailScreen
import com.walletwise.app.feature.expenses.ExpensesScreen
import com.walletwise.app.feature.notifications.NotificationsScreen
import com.walletwise.app.feature.onboarding.OnboardingScreen
import com.walletwise.app.feature.prediction.PredictionScreen
import com.walletwise.app.feature.reports.ReportsScreen
import com.walletwise.app.feature.settings.ProfileScreen
import com.walletwise.app.feature.settings.SettingsScreen

@Composable
fun WalletWiseNavGraph(
    navController: NavHostController = rememberNavController()
) {
    val sharedRepository = remember { WalletRepository() }
    val userProfile by sharedRepository.user.collectAsState(initial = com.walletwise.app.core.model.UserProfile())

    WalletWiseTheme(themeMode = userProfile.themeMode) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        var showQuickActionsSheet by remember { mutableStateOf(false) }
        var showAddExpenseSheet by remember { mutableStateOf(false) }
        var showCreateBudgetSheet by remember { mutableStateOf(false) }

        val bottomBarRoutes = listOf(
            Screen.Dashboard.route,
            Screen.Expenses.route,
            Screen.Reports.route,
            Screen.Profile.route
        )

        val showBottomBar = currentRoute in bottomBarRoutes

        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    WalletBottomBar(
                        currentRoute = currentRoute,
                        onNavigate = { route ->
                            navController.navigate(route) {
                                popUpTo(Screen.Dashboard.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onFabClick = { showQuickActionsSheet = true }
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Screen.Splash.route,
                    enterTransition = { fadeIn(animationSpec = tween(300)) },
                    exitTransition = { fadeOut(animationSpec = tween(300)) }
                ) {
                    // Auth & Onboarding Routes
                    composable(Screen.Splash.route) {
                        SplashScreen(
                            onNavigateNext = {
                                navController.navigate(Screen.Onboarding.route) {
                                    popUpTo(Screen.Splash.route) { inclusive = true }
                                }
                            }
                        )
                    }

                    composable(Screen.Onboarding.route) {
                        OnboardingScreen(
                            onFinish = {
                                navController.navigate(Screen.Welcome.route) {
                                    popUpTo(Screen.Onboarding.route) { inclusive = true }
                                }
                            },
                            onSkip = {
                                navController.navigate(Screen.Welcome.route) {
                                    popUpTo(Screen.Onboarding.route) { inclusive = true }
                                }
                            }
                        )
                    }

                    composable(Screen.Welcome.route) {
                        WelcomeScreen(
                            onLoginClick = { navController.navigate(Screen.Login.route) },
                            onRegisterClick = { navController.navigate(Screen.Register.route) }
                        )
                    }

                    composable(Screen.Login.route) {
                        LoginScreen(
                            onLoginSuccess = {
                                navController.navigate(Screen.Dashboard.route) {
                                    popUpTo(Screen.Welcome.route) { inclusive = true }
                                }
                            },
                            onNavigateRegister = { navController.navigate(Screen.Register.route) },
                            onNavigateForgotPassword = { navController.navigate(Screen.ForgotPassword.route) }
                        )
                    }

                    composable(Screen.Register.route) {
                        RegisterScreen(
                            onRegisterSuccess = {
                                navController.navigate(Screen.Dashboard.route) {
                                    popUpTo(Screen.Welcome.route) { inclusive = true }
                                }
                            },
                            onNavigateLogin = { navController.navigate(Screen.Login.route) }
                        )
                    }

                    composable(Screen.ForgotPassword.route) {
                        ForgotPasswordScreen(
                            onResetSent = {},
                            onBackToLogin = { navController.popBackStack() }
                        )
                    }

                    // App Feature Routes
                    composable(Screen.Dashboard.route) {
                        DashboardScreen(
                            onNavigateExpenses = { navController.navigate(Screen.Expenses.route) },
                            onNavigateBudget = { navController.navigate(Screen.Budget.route) },
                            onNavigatePrediction = { navController.navigate(Screen.Prediction.route) },
                            onNavigateNotifications = { navController.navigate(Screen.Notifications.route) },
                            onNavigateExpenseDetail = { id ->
                                navController.navigate(Screen.ExpenseDetail.createRoute(id))
                            }
                        )
                    }

                    composable(Screen.Expenses.route) {
                        ExpensesScreen(
                            onNavigateExpenseDetail = { id ->
                                navController.navigate(Screen.ExpenseDetail.createRoute(id))
                            },
                            onOpenAddExpense = { showAddExpenseSheet = true }
                        )
                    }

                    composable(
                        route = Screen.ExpenseDetail.route,
                        arguments = listOf(navArgument("expenseId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val expenseId = backStackEntry.arguments?.getString("expenseId") ?: ""
                        ExpenseDetailScreen(
                            expenseId = expenseId,
                            onBack = { navController.popBackStack() },
                            repository = sharedRepository
                        )
                    }

                    composable(Screen.Budget.route) {
                        BudgetScreen(
                            onOpenCreateBudget = { showCreateBudgetSheet = true }
                        )
                    }

                    composable(Screen.Reports.route) {
                        ReportsScreen()
                    }

                    composable(Screen.Prediction.route) {
                        PredictionScreen()
                    }

                    composable(Screen.Notifications.route) {
                        NotificationsScreen(
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable(Screen.Profile.route) {
                        ProfileScreen(
                            onNavigateSettings = { navController.navigate(Screen.Settings.route) }
                        )
                    }

                    composable(Screen.Settings.route) {
                        SettingsScreen(
                            onNavigateBack = { navController.popBackStack() },
                            onLogout = {
                                navController.navigate(Screen.Welcome.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }

        // Modal Bottom Sheets
        if (showQuickActionsSheet) {
            QuickActionsBottomSheet(
                onDismiss = { showQuickActionsSheet = false },
                onAddExpense = { showAddExpenseSheet = true },
                onScanReceipt = { showAddExpenseSheet = true },
                onCreateBudget = { showCreateBudgetSheet = true },
                onAiAssistant = { navController.navigate(Screen.Prediction.route) }
            )
        }

        if (showAddExpenseSheet) {
            AddEditExpenseSheet(
                onDismiss = { showAddExpenseSheet = false },
                onSaveExpense = { expense ->
                    sharedRepository.addExpense(expense)
                }
            )
        }

        if (showCreateBudgetSheet) {
            CreateEditBudgetSheet(
                onDismiss = { showCreateBudgetSheet = false },
                onSaveBudget = { budget ->
                    sharedRepository.addBudget(budget)
                }
            )
        }
    }
}
