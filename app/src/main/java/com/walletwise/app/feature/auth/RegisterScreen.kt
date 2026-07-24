package com.walletwise.app.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.walletwise.app.core.designsystem.*
import com.walletwise.app.core.designsystem.components.PrimaryButton
import com.walletwise.app.core.designsystem.components.WalletTextField

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
                text = "Create Account",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = WalletTextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Start tracking expenses & growing your savings today",
                fontSize = 14.sp,
                color = WalletTextSecondary
            )

            Spacer(modifier = Modifier.height(28.dp))

            WalletTextField(
                value = name,
                onValueChange = { name = it },
                label = "Full Name",
                placeholder = "Alex Vance",
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

            Spacer(modifier = Modifier.height(14.dp))

            WalletTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                placeholder = "Minimum 8 characters",
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = "Password",
                        tint = WalletPrimary
                    )
                }
            )
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            PrimaryButton(
                text = "Create Account",
                onClick = onRegisterSuccess
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Already have an account? ", fontSize = 14.sp, color = WalletTextSecondary)
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
