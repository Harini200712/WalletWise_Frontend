package com.walletwise.app.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.walletwise.app.core.designsystem.*
import com.walletwise.app.core.designsystem.components.PrimaryButton
import com.walletwise.app.core.designsystem.components.WalletTextField

@Composable
fun ForgotPasswordScreen(
    onResetSent: () -> Unit,
    onBackToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var sentMessage by remember { mutableStateOf<String?>(null) }

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
                text = "Reset Password",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = WalletTextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Enter your registered email address and we'll send reset instructions",
                fontSize = 14.sp,
                color = WalletTextSecondary
            )

            Spacer(modifier = Modifier.height(28.dp))

            WalletTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email Address",
                placeholder = "alex@example.com",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Email,
                        contentDescription = "Email",
                        tint = WalletPrimary
                    )
                }
            )

            if (sentMessage != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = sentMessage!!,
                    color = WalletSuccess,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            PrimaryButton(
                text = "Send Instructions",
                onClick = {
                    sentMessage = "Reset link dispatched to $email!"
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
