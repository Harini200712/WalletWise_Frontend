package com.walletwise.app.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.walletwise.app.core.designsystem.*
import com.walletwise.app.core.designsystem.components.PrimaryButton
import com.walletwise.app.core.designsystem.components.SuccessState
import com.walletwise.app.core.designsystem.components.WalletTextField
import com.walletwise.app.core.ui.UiState

@Composable
fun ForgotPasswordScreen(
    onResetSent: () -> Unit,
    onBackToLogin: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    val email by viewModel.forgotEmail.collectAsState()
    val emailError by viewModel.forgotEmailError.collectAsState()
    val forgotState by viewModel.forgotState.collectAsState()

    val isValid = email.contains("@") && email.contains(".")

    if (forgotState is UiState.Success) {
        SuccessState(
            title = "Check Your Email",
            description = "We've dispatched password recovery instructions to $email.",
            actionText = "Return to Sign In",
            onActionClick = onBackToLogin,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Reset Password",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Enter your registered email address and we'll send reset instructions",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(28.dp))

                WalletTextField(
                    value = email,
                    onValueChange = { viewModel.onForgotEmailChange(it) },
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
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                PrimaryButton(
                    text = "Send Instructions",
                    enabled = isValid,
                    isLoading = forgotState is UiState.Loading,
                    onClick = {
                        viewModel.sendPasswordReset(onSuccess = {})
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Back to Sign In",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = WalletPrimary,
                        modifier = Modifier.clickable { onBackToLogin() }
                    )
                }
            }
        }
    }
}
