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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.walletwise.app.core.designsystem.*
import com.walletwise.app.core.designsystem.components.PrimaryButton
import com.walletwise.app.core.designsystem.components.ProgressCard
import com.walletwise.app.core.designsystem.components.SecondaryButton
import com.walletwise.app.core.designsystem.components.WalletCard
import com.walletwise.app.core.designsystem.components.WalletTextField

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(),
    onNavigateSettings: () -> Unit = {}
) {
    val user by viewModel.rawUser.collectAsState()

    var isEditing by remember { mutableStateOf(false) }

    var editName by remember(user) { mutableStateOf(user.name) }
    var editEmail by remember(user) { mutableStateOf(user.email) }
    var editPhone by remember(user) { mutableStateOf(user.phone) }
    var editIncome by remember(user) { mutableStateOf(user.monthlyIncome.toString()) }
    var editOccupation by remember(user) { mutableStateOf(user.occupation) }

    var showPhotoPickerMsg by remember { mutableStateOf(false) }

    if (showPhotoPickerMsg) {
        AlertDialog(
            onDismissRequest = { showPhotoPickerMsg = false },
            title = { Text("Change Profile Picture") },
            text = { Text("Gallery picker simulator: Profile picture updated successfully.") },
            confirmButton = {
                TextButton(onClick = { showPhotoPickerMsg = false }) {
                    Text("OK", color = WalletPrimary)
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "My Profile",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Manage your account details and income info",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onNavigateSettings) {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = "Settings",
                        tint = WalletPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        // Profile Completion Progress Card
        item {
            ProgressCard(
                title = "Profile Completion",
                progress = user.completionPercentage / 100f,
                progressText = "${user.completionPercentage}%",
                subtitle = if (user.completionPercentage == 100) "Your profile is fully complete!" else "Fill in all fields to unlock personalized AI suggestions.",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                progressColor = WalletPrimary
            )
        }

        // Avatar & Details Hero
        item {
            Spacer(modifier = Modifier.height(12.dp))
            WalletCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(WalletPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = user.name.take(1).ifEmpty { "U" },
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(WalletAccentOrange)
                                .clickable { showPhotoPickerMsg = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.PhotoCamera,
                                contentDescription = "Change Photo",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = user.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = user.occupation,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Form Fields
        item {
            Spacer(modifier = Modifier.height(20.dp))
            WalletCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Personal Details",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (!isEditing) {
                        TextButton(onClick = { isEditing = true }) {
                            Icon(Icons.Rounded.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Edit", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                WalletTextField(
                    value = editName,
                    onValueChange = { editName = it },
                    label = "Full Name",
                    enabled = isEditing,
                    leadingIcon = { Icon(Icons.Rounded.Person, contentDescription = null, tint = WalletPrimary) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                WalletTextField(
                    value = editEmail,
                    onValueChange = { editEmail = it },
                    label = "Email Address",
                    enabled = isEditing,
                    leadingIcon = { Icon(Icons.Rounded.Email, contentDescription = null, tint = WalletPrimary) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                WalletTextField(
                    value = editPhone,
                    onValueChange = { editPhone = it },
                    label = "Phone Number",
                    enabled = isEditing,
                    leadingIcon = { Icon(Icons.Rounded.Phone, contentDescription = null, tint = WalletPrimary) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                WalletTextField(
                    value = editIncome,
                    onValueChange = { editIncome = it },
                    label = "Monthly Income (${user.currencySymbol})",
                    enabled = isEditing,
                    leadingIcon = { Icon(Icons.Rounded.AccountBalance, contentDescription = null, tint = WalletPrimary) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                WalletTextField(
                    value = editOccupation,
                    onValueChange = { editOccupation = it },
                    label = "Occupation",
                    enabled = isEditing,
                    leadingIcon = { Icon(Icons.Rounded.Work, contentDescription = null, tint = WalletPrimary) }
                )

                if (isEditing) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SecondaryButton(
                            text = "Cancel",
                            onClick = {
                                editName = user.name
                                editEmail = user.email
                                editPhone = user.phone
                                editIncome = user.monthlyIncome.toString()
                                editOccupation = user.occupation
                                isEditing = false
                            },
                            modifier = Modifier.weight(1f)
                        )
                        PrimaryButton(
                            text = "Save Changes",
                            onClick = {
                                val parsedIncome = editIncome.toDoubleOrNull() ?: user.monthlyIncome
                                viewModel.saveProfile(
                                    name = editName,
                                    email = editEmail,
                                    phone = editPhone,
                                    income = parsedIncome,
                                    occupation = editOccupation
                                )
                                isEditing = false
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}
