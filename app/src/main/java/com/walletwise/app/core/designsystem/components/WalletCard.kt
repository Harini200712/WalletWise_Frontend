package com.walletwise.app.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.walletwise.app.core.designsystem.CardShape
import com.walletwise.app.core.designsystem.WalletPrimary
import com.walletwise.app.core.designsystem.WalletPrimaryDark
import com.walletwise.app.core.designsystem.WalletPrimaryLight
import com.walletwise.app.core.designsystem.WalletTextPrimary
import com.walletwise.app.core.designsystem.WalletTextSecondary

@Composable
fun WalletCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    elevation: Dp = 6.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val cardModifier = if (onClick != null) {
        modifier.clickable { onClick() }
    } else {
        modifier
    }

    Card(
        modifier = cardModifier
            .shadow(
                elevation = elevation,
                shape = CardShape,
                spotColor = Color(0x1A5E7BFF),
                ambientColor = Color(0x0D000000)
            ),
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        content = {
            Column(
                modifier = Modifier.padding(20.dp),
                content = content
            )
        }
    )
}

@Composable
fun GradientWalletCard(
    modifier: Modifier = Modifier,
    gradientColors: List<Color> = listOf(WalletPrimary, WalletPrimaryDark),
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = 10.dp,
                shape = CardShape,
                spotColor = WalletPrimary.copy(alpha = 0.4f)
            )
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .background(Brush.linearGradient(gradientColors))
                .padding(22.dp)
        ) {
            Column(content = content)
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    accentColor: Color = WalletPrimary,
    onClick: (() -> Unit)? = null
) {
    WalletCard(
        modifier = modifier,
        elevation = 4.dp,
        onClick = onClick
    ) {
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = WalletTextSecondary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = WalletTextPrimary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = subtitle,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = accentColor
        )
    }
}

@Composable
fun InfoCard(
    title: String,
    description: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    iconBgColor: Color = WalletPrimaryLight,
    iconTint: Color = WalletPrimary,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    WalletCard(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        if (actionText != null && onActionClick != null) {
            Spacer(modifier = Modifier.height(12.dp))
            TextButton(
                onClick = onActionClick,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = actionText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = WalletPrimary
                )
            }
        }
    }
}

@Composable
fun ProgressCard(
    title: String,
    progress: Float,
    progressText: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    progressColor: Color = WalletPrimary
) {
    WalletCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = progressText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = progressColor
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        LinearProgressIndicator(
            progress = progress.coerceIn(0f, 1f),
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = progressColor,
            trackColor = progressColor.copy(alpha = 0.15f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = subtitle,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String = "Search expenses...",
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(text = placeholder, color = WalletTextSecondary, fontSize = 14.sp)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = WalletTextSecondary
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear",
                        tint = WalletTextSecondary
                    )
                }
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = CardShape,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = WalletPrimary,
            unfocusedBorderColor = Color(0xFFE8ECF6),
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
    )
}

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        if (actionText != null && onActionClick != null) {
            TextButton(onClick = onActionClick) {
                Text(
                    text = actionText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = WalletPrimary
                )
            }
        }
    }
}
