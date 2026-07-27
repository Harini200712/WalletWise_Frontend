package com.walletwise.app.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.walletwise.app.core.designsystem.*
import com.walletwise.app.core.designsystem.components.ConfirmationDialog
import com.walletwise.app.core.designsystem.components.SecondaryButton
import com.walletwise.app.core.designsystem.components.WalletCard
import com.walletwise.app.core.designsystem.components.WalletDialog

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val user by viewModel.rawUser.collectAsState()

    var showThemeDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showCurrencyDialog by remember { mutableStateOf(false) }
    var showSecurityDialog by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showPrivacyModal by remember { mutableStateOf(false) }
    var showTermsModal by remember { mutableStateOf(false) }
    var showLogoutConfirm by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "App Settings",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 40.dp)
            ) {
                // Section 1: Appearance & Localization
                item {
                    Text(
                        text = "Appearance & Language",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = WalletPrimary,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                    )
                    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                        SettingItemTile(
                            title = "Theme",
                            subtitle = "Current: ${user.themeMode}",
                            icon = Icons.Rounded.DarkMode,
                            onClick = { showThemeDialog = true }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        SettingItemTile(
                            title = "Language",
                            subtitle = "Current: ${user.language}",
                            icon = Icons.Rounded.Language,
                            onClick = { showLanguageDialog = true }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        SettingItemTile(
                            title = "Currency Symbol",
                            subtitle = "Current: ${user.currencySymbol}",
                            icon = Icons.Rounded.AttachMoney,
                            onClick = { showCurrencyDialog = true }
                        )
                    }
                }

                // Section 2: Preferences & Security
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Preferences & Security",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = WalletPrimary,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                    )
                    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                        SettingSwitchTile(
                            title = "Push Notifications",
                            subtitle = "Budget thresholds & AI alert reminders",
                            icon = Icons.Rounded.Notifications,
                            checked = user.notificationsEnabled,
                            onCheckedChange = { viewModel.toggleNotifications(it) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        SettingItemTile(
                            title = "Security & Biometrics",
                            subtitle = if (user.biometricEnabled) "Biometric Lock Active" else "Biometrics Disabled",
                            icon = Icons.Rounded.Security,
                            onClick = { showSecurityDialog = true }
                        )
                    }
                }

                // Section 3: Legal & Support
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Support & Legal",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = WalletPrimary,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                    )
                    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                        SettingItemTile(
                            title = "Help & Support",
                            subtitle = "FAQs & Customer Assistance",
                            icon = Icons.Rounded.HelpOutline,
                            onClick = { showHelpDialog = true }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        SettingItemTile(
                            title = "About WalletWise",
                            subtitle = "Version 1.0.0 (Build 2026.07)",
                            icon = Icons.Rounded.Info,
                            onClick = { showAboutDialog = true }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        SettingItemTile(
                            title = "Privacy Policy",
                            subtitle = "Data protection & privacy terms",
                            icon = Icons.Rounded.PrivacyTip,
                            onClick = { showPrivacyModal = true }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        SettingItemTile(
                            title = "Terms of Service",
                            subtitle = "End-User License Agreement",
                            icon = Icons.Rounded.Description,
                            onClick = { showTermsModal = true }
                        )
                    }
                }

                // Section 4: Account Actions
                item {
                    Spacer(modifier = Modifier.height(28.dp))
                    Box(modifier = Modifier.padding(horizontal = 24.dp)) {
                        SecondaryButton(
                            text = "Log Out of WalletWise",
                            onClick = { showLogoutConfirm = true },
                            containerColor = Color(0xFFFFF0F0),
                            contentColor = WalletError
                        )
                    }
                }
            }
        }
    }

    // Dialogs
    if (showThemeDialog) {
        WalletDialog(
            onDismissRequest = { showThemeDialog = false },
            title = "Select Theme",
            icon = Icons.Rounded.DarkMode
        ) {
            listOf("Light", "Dark", "System").forEach { mode ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.setThemeMode(mode)
                            showThemeDialog = false
                        }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = user.themeMode == mode,
                        onClick = {
                            viewModel.setThemeMode(mode)
                            showThemeDialog = false
                        },
                        colors = RadioButtonDefaults.colors(selectedColor = WalletPrimary)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = mode, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }

    if (showLanguageDialog) {
        WalletDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = "Select Language",
            icon = Icons.Rounded.Language
        ) {
            listOf("English", "Tamil", "Hindi").forEach { lang ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.setLanguage(lang)
                            showLanguageDialog = false
                        }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = user.language == lang,
                        onClick = {
                            viewModel.setLanguage(lang)
                            showLanguageDialog = false
                        },
                        colors = RadioButtonDefaults.colors(selectedColor = WalletPrimary)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = lang, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }

    if (showCurrencyDialog) {
        WalletDialog(
            onDismissRequest = { showCurrencyDialog = false },
            title = "Select Currency Symbol",
            icon = Icons.Rounded.AttachMoney
        ) {
            listOf("₹" to "Indian Rupee (INR)", "$" to "US Dollar (USD)", "€" to "Euro (EUR)", "£" to "British Pound (GBP)").forEach { (symbol, name) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.setCurrency(symbol)
                            showCurrencyDialog = false
                        }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = user.currencySymbol == symbol,
                        onClick = {
                            viewModel.setCurrency(symbol)
                            showCurrencyDialog = false
                        },
                        colors = RadioButtonDefaults.colors(selectedColor = WalletPrimary)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "$symbol - $name", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }

    if (showSecurityDialog) {
        WalletDialog(
            onDismissRequest = { showSecurityDialog = false },
            title = "Security & Biometrics",
            icon = Icons.Rounded.Security
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Biometric Authentication", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                    Switch(
                        checked = user.biometricEnabled,
                        onCheckedChange = { viewModel.toggleBiometrics(it) }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                SecondaryButton(
                    text = "Setup Security PIN (Mock)",
                    onClick = { showSecurityDialog = false }
                )
            }
        }
    }

    if (showHelpDialog) {
        WalletDialog(
            onDismissRequest = { showHelpDialog = false },
            title = "Help & Support",
            icon = Icons.Rounded.HelpOutline
        ) {
            Column {
                Text(
                    text = "Need assistance with WalletWise? Contact support at support@walletwise.io or read our offline FAQ guide.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                SecondaryButton(text = "Close", onClick = { showHelpDialog = false })
            }
        }
    }

    if (showAboutDialog) {
        WalletDialog(
            onDismissRequest = { showAboutDialog = false },
            title = "About WalletWise",
            icon = Icons.Rounded.Info
        ) {
            Column {
                Text(
                    text = "WalletWise v1.0.0\nAI Powered Smart Expense & Budget Management System.\nBuilt with Jetpack Compose & Material Design 3.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                SecondaryButton(text = "Close", onClick = { showAboutDialog = false })
            }
        }
    }

    if (showPrivacyModal) {
        WalletDialog(
            onDismissRequest = { showPrivacyModal = false },
            title = "Privacy Policy",
            icon = Icons.Rounded.PrivacyTip
        ) {
            Column {
                Text(
                    text = "Your privacy is paramount. WalletWise operates 100% locally with mock state repositories. No personal data is harvested or transmitted externally.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                SecondaryButton(text = "Accept & Close", onClick = { showPrivacyModal = false })
            }
        }
    }

    if (showTermsModal) {
        WalletDialog(
            onDismissRequest = { showTermsModal = false },
            title = "Terms of Service",
            icon = Icons.Rounded.Description
        ) {
            Column {
                Text(
                    text = "By using WalletWise, you agree to local budget tracking policies and simulated AI financial predictions.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                SecondaryButton(text = "Close", onClick = { showTermsModal = false })
            }
        }
    }

    ConfirmationDialog(
        showDialog = showLogoutConfirm,
        onDismiss = { showLogoutConfirm = false },
        onConfirm = onLogout,
        title = "Confirm Logout",
        message = "Are you sure you want to log out of WalletWise?",
        confirmText = "Log Out",
        isDestructive = true
    )
}

@Composable
private fun SettingItemTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    WalletCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(WalletPrimaryLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = WalletPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                    Text(text = subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = "Navigate",
                tint = WalletTextSecondary
            )
        }
    }
}

@Composable
private fun SettingSwitchTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    WalletCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(WalletPrimaryLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = WalletPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                    Text(text = subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(checkedTrackColor = WalletPrimary)
            )
        }
    }
}
