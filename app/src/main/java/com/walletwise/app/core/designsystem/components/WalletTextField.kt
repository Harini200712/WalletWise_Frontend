package com.walletwise.app.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.walletwise.app.core.designsystem.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = label, fontSize = 14.sp) },
            placeholder = { Text(text = placeholder, color = WalletTextSecondary, fontSize = 14.sp) },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            isError = isError,
            shape = TextFieldShape,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = WalletPrimary,
                unfocusedBorderColor = WalletDivider,
                focusedContainerColor = WalletSurface,
                unfocusedContainerColor = WalletBackground,
                errorBorderColor = WalletError
            ),
            singleLine = true
        )
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = WalletError,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String = "Search expenses, merchants, categories...",
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
        placeholder = { Text(placeholder, color = WalletTextSecondary, fontSize = 14.sp) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Search",
                tint = WalletPrimary
            )
        },
        shape = TextFieldShape,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = WalletPrimary,
            unfocusedBorderColor = WalletDivider,
            focusedContainerColor = WalletSurface,
            unfocusedContainerColor = WalletSurface
        ),
        singleLine = true
    )
}
