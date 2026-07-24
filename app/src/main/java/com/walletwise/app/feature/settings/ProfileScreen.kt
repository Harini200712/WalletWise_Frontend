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
import com.walletwise.app.core.designsystem.*
import com.walletwise.app.core.designsystem.components.PrimaryButton
import com.walletwise.app.core.designsystem.components.SecondaryButton
import com.walletwise.app.core.designsystem.components.WalletCard
import com.walletwise.app.core.designsystem.components.WalletTextField

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = ProfileViewModel(),
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val user = uiState.user

    var isEditingName by remember { mutableStateOf(false) }
    var editNameText by remember { mutableStateOf(user?.name ?: "Alex Vance") }
    var editEmailText by remember { mutableStateOf(user?.email ?: "alex@walletwise.ai") }

    var darkThemeEnabled by remember { mutableStateOf(false) }
    var biometricEnabled by remember { mutableStateOf(user?.biometricEnabled ?: true) }
    var selectedCurrency by remember { mutableStateOf("₹ (INR)") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(WalletBackground),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
                Text(
                    text = "Account & Profile",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = WalletTextPrimary
                )
                Text(
                    text = "Manage personal profile preferences and security options",
                    fontSize = 13.sp,
                    color = WalletTextSecondary
                )
            }
        }

        // Editable Profile Hero Card
        item {
            WalletCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                elevation = 6.dp
            ) {
                if (isEditingName) {
                    Column {
                        Text("Edit Profile Details", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = WalletTextPrimary)
                        Spacer(modifier = Modifier.height(10.dp))
                        WalletTextField(
                            value = editNameText,
                            onValueChange = { editNameText = it },
                            label = "Full Name"
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        WalletTextField(
                            value = editEmailText,
                            onValueChange = { editEmailText = it },
                            label = "Email Address"
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        PrimaryButton(
                            text = "Save Profile Changes",
                            onClick = {
                                user?.let {
                                    viewModel.updateUserProfile(it.copy(name = editNameText, email = editEmailText))
                                }
                                isEditingName = false
                            }
                        )
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(WalletPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (user?.name ?: "A").take(1),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = user?.name ?: "Alex Vance",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = WalletTextPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(ChipShape)
                                        .background(WalletAccentOrange.copy(alpha = 0.2f))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "PRO",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = WalletAccentOrange
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = user?.email ?: "alex@walletwise.ai",
                                fontSize = 13.sp,
                                color = WalletTextSecondary
                            )
                        }

                        IconButton(onClick = { isEditingName = true }) {
                            Icon(Icons.Rounded.Edit, contentDescription = "Edit Profile", tint = WalletPrimary)
                        }
                    }
                }
            }
        }

        // Settings Tiles Group
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Preferences",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = WalletTextPrimary,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                SettingSwitchTile(
                    title = "Dark Theme",
                    subtitle = "Toggle modern dark mode palette",
                    icon = Icons.Rounded.DarkMode,
                    checked = darkThemeEnabled,
                    onCheckedChange = { darkThemeEnabled = it }
                )

                Spacer(modifier = Modifier.height(8.dp))

                SettingSwitchTile(
                    title = "Biometric Security",
                    subtitle = "Require fingerprint / Face Unlock on launch",
                    icon = Icons.Rounded.Fingerprint,
                    checked = biometricEnabled,
                    onCheckedChange = { biometricEnabled = it }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "General Settings",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = WalletTextPrimary,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                SettingActionTile(
                    title = "Default Currency",
                    subtitle = selectedCurrency,
                    icon = Icons.Rounded.CurrencyRupee
                )
                Spacer(modifier = Modifier.height(8.dp))
                SettingActionTile(
                    title = "Privacy & Cloud Sync",
                    subtitle = "Synchronized with Google Cloud & Firebase",
                    icon = Icons.Rounded.Security
                )
                Spacer(modifier = Modifier.height(8.dp))
                SettingActionTile(
                    title = "Help & Support",
                    subtitle = "FAQ, AI chat support, app version 1.0.0",
                    icon = Icons.Rounded.HelpOutline
                )
            }
        }

        // Logout Button
        item {
            Spacer(modifier = Modifier.height(28.dp))
            Box(modifier = Modifier.padding(horizontal = 24.dp)) {
                SecondaryButton(
                    text = "Log Out of WalletWise",
                    onClick = onLogout,
                    containerColor = Color(0xFFFFF0F0),
                    contentColor = WalletError
                )
            }
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
                    Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = WalletTextPrimary)
                    Text(text = subtitle, fontSize = 12.sp, color = WalletTextSecondary)
                }
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = WalletPrimary
                )
            )
        }
    }
}

@Composable
private fun SettingActionTile(
    title: String,
    subtitle: String,
    icon: ImageVector
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
                    Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = WalletTextPrimary)
                    Text(text = subtitle, fontSize = 12.sp, color = WalletTextSecondary)
                }
            }

            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = "Go",
                tint = WalletTextSecondary
            )
        }
    }
}
