package com.walletwise.app.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.walletwise.app.core.designsystem.*
import com.walletwise.app.core.designsystem.components.PrimaryButton
import com.walletwise.app.core.designsystem.components.WalletTextField

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateLogin: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    val fullName by viewModel.regFullName.collectAsState()
    val email by viewModel.regEmail.collectAsState()
    val phone by viewModel.regPhone.collectAsState()
    val password by viewModel.regPassword.collectAsState()
    val confirmPassword by viewModel.regConfirmPassword.collectAsState()

    val emailError by viewModel.regEmailError.collectAsState()
    val phoneError by viewModel.regPhoneError.collectAsState()
    val passwordError by viewModel.regPasswordError.collectAsState()
    val confirmPasswordError by viewModel.regConfirmPasswordError.collectAsState()

    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    val strength = viewModel.getPasswordStrength()
    val isValid = viewModel.isRegisterValid()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Create Account",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Start tracking expenses & growing your savings today",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            WalletTextField(
                value = fullName,
                onValueChange = { viewModel.regFullName.value = it },
                label = "Full Name",
                placeholder = "Alex Morgan",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Person,
                        contentDescription = "Name",
                        tint = WalletPrimary
                    )
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            WalletTextField(
                value = email,
                onValueChange = { viewModel.onRegEmailChange(it) },
                label = "Email Address",
                placeholder = "alex.morgan@walletwise.io",
                errorMessage = emailError,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Email,
                        contentDescription = "Email",
                        tint = WalletPrimary
                    )
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            WalletTextField(
                value = phone,
                onValueChange = { viewModel.onRegPhoneChange(it) },
                label = "Phone Number",
                placeholder = "+91 98765 43210",
                errorMessage = phoneError,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Phone,
                        contentDescription = "Phone",
                        tint = WalletPrimary
                    )
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            WalletTextField(
                value = password,
                onValueChange = { viewModel.onRegPasswordChange(it) },
                label = "Password",
                placeholder = "Minimum 6 characters",
                errorMessage = passwordError,
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = "Password",
                        tint = WalletPrimary
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                            contentDescription = "Toggle Password",
                            tint = WalletTextSecondary
                        )
                    }
                }
            )

            // Password Strength Bar
            if (password.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LinearProgressIndicator(
                        progress = strength.progress,
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = when (strength) {
                            PasswordStrength.STRONG -> Color(0xFF4CAF50)
                            PasswordStrength.FAIR -> WalletAccentOrange
                            else -> MaterialTheme.colorScheme.error
                        },
                        trackColor = Color(0xFFE8ECF6)
                    )
                    Text(
                        text = "Strength: ${strength.label}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = when (strength) {
                            PasswordStrength.STRONG -> Color(0xFF4CAF50)
                            PasswordStrength.FAIR -> WalletAccentOrange
                            else -> MaterialTheme.colorScheme.error
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            WalletTextField(
                value = confirmPassword,
                onValueChange = { viewModel.onRegConfirmPasswordChange(it) },
                label = "Confirm Password",
                placeholder = "Re-enter password",
                errorMessage = confirmPasswordError,
                visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = "Confirm Password",
                        tint = WalletPrimary
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                        Icon(
                            imageVector = if (isConfirmPasswordVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                            contentDescription = "Toggle Confirm Password",
                            tint = WalletTextSecondary
                        )
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            PrimaryButton(
                text = "Create Account",
                enabled = isValid,
                onClick = {
                    viewModel.performRegister(onSuccess = onRegisterSuccess)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "Already have an account? ",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Sign In",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = WalletPrimary,
                    modifier = Modifier.clickable { onNavigateLogin() }
                )
            }
        }
    }
}
