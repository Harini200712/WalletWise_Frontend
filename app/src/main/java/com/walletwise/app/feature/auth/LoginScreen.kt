package com.walletwise.app.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.walletwise.app.core.designsystem.*
import com.walletwise.app.core.designsystem.components.PrimaryButton
import com.walletwise.app.core.designsystem.components.SecondaryButton
import com.walletwise.app.core.designsystem.components.WalletTextField

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateRegister: () -> Unit,
    onNavigateForgotPassword: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    val email by viewModel.loginEmail.collectAsState()
    val password by viewModel.loginPassword.collectAsState()
    val rememberMe by viewModel.rememberMe.collectAsState()
    val emailError by viewModel.loginEmailError.collectAsState()
    val passwordError by viewModel.loginPasswordError.collectAsState()
    val loginState by viewModel.loginState.collectAsState()

    var isPasswordVisible by remember { mutableStateOf(false) }

    val isValid = viewModel.isLoginValid()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Welcome Back!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Sign in to access your WalletWise dashboard & AI insights",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            WalletTextField(
                value = email,
                onValueChange = { viewModel.onLoginEmailChange(it) },
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

            Spacer(modifier = Modifier.height(16.dp))

            WalletTextField(
                value = password,
                onValueChange = { viewModel.onLoginPasswordChange(it) },
                label = "Password",
                placeholder = "••••••••",
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

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { viewModel.rememberMe.value = it },
                        colors = CheckboxDefaults.colors(checkedColor = WalletPrimary)
                    )
                    Text(
                        text = "Remember Me",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = "Forgot Password?",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = WalletPrimary,
                    modifier = Modifier.clickable { onNavigateForgotPassword() }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            PrimaryButton(
                text = "Sign In",
                enabled = isValid,
                onClick = {
                    viewModel.performLogin(onSuccess = onLoginSuccess)
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            SecondaryButton(
                text = "Sign In with Google (UI Demo)",
                onClick = {
                    viewModel.performLogin(onSuccess = onLoginSuccess)
                },
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "Don't have an account? ",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Sign Up",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = WalletPrimary,
                    modifier = Modifier.clickable { onNavigateRegister() }
                )
            }
        }
    }
}
