package com.walletwise.app.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.walletwise.app.core.designsystem.*
import com.walletwise.app.core.designsystem.components.PrimaryButton
import com.walletwise.app.core.designsystem.components.SecondaryButton
import com.walletwise.app.core.designsystem.components.WalletTextField

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateRegister: () -> Unit,
    onNavigateForgotPassword: () -> Unit
) {
    var email by remember { mutableStateOf("alex.vance@walletwise.ai") }
    var password by remember { mutableStateOf("password123") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WalletBackground)
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Welcome Back!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = WalletTextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Sign in to access your WalletWise dashboard & AI insights",
                fontSize = 14.sp,
                color = WalletTextSecondary
            )

            Spacer(modifier = Modifier.height(32.dp))

            WalletTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email Address",
                placeholder = "name@example.com",
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
                onValueChange = { password = it },
                label = "Password",
                placeholder = "••••••••",
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = "Password",
                        tint = WalletPrimary
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Forgot Password?",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = WalletPrimary,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { onNavigateForgotPassword() }
            )
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            PrimaryButton(
                text = "Sign In",
                onClick = onLoginSuccess
            )

            Spacer(modifier = Modifier.height(12.dp))

            SecondaryButton(
                text = "Sign In with Google",
                onClick = onLoginSuccess,
                containerColor = WalletSurface,
                contentColor = WalletTextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Don't have an account? ", fontSize = 14.sp, color = WalletTextSecondary)
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
