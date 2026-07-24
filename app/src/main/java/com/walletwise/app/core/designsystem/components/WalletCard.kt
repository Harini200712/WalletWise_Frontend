package com.walletwise.app.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.walletwise.app.core.designsystem.CardShape
import com.walletwise.app.core.designsystem.WalletPrimary
import com.walletwise.app.core.designsystem.WalletPrimaryDark
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
