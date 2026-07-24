package com.walletwise.app.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.walletwise.app.core.designsystem.*
import com.walletwise.app.navigation.Screen

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomNavItem(Screen.Dashboard.route, "Home", Icons.Rounded.Home)
    object Expenses : BottomNavItem(Screen.Expenses.route, "Expenses", Icons.Rounded.ReceiptLong)
    object Reports : BottomNavItem(Screen.Reports.route, "Reports", Icons.Rounded.PieChart)
    object Profile : BottomNavItem(Screen.Profile.route, "Profile", Icons.Rounded.Person)
}

@Composable
fun WalletBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    onFabClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Bottom Navigation Card Background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = ButtonShape,
                    spotColor = Color(0x335E7BFF)
                )
                .background(WalletSurface, shape = ButtonShape)
                .padding(horizontal = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left items
                BottomNavItemView(
                    item = BottomNavItem.Home,
                    isSelected = currentRoute == BottomNavItem.Home.route,
                    onClick = { onNavigate(BottomNavItem.Home.route) }
                )
                BottomNavItemView(
                    item = BottomNavItem.Expenses,
                    isSelected = currentRoute == BottomNavItem.Expenses.route,
                    onClick = { onNavigate(BottomNavItem.Expenses.route) }
                )

                // Spacer for central FAB
                Spacer(modifier = Modifier.width(52.dp))

                // Right items
                BottomNavItemView(
                    item = BottomNavItem.Reports,
                    isSelected = currentRoute == BottomNavItem.Reports.route,
                    onClick = { onNavigate(BottomNavItem.Reports.route) }
                )
                BottomNavItemView(
                    item = BottomNavItem.Profile,
                    isSelected = currentRoute == BottomNavItem.Profile.route,
                    onClick = { onNavigate(BottomNavItem.Profile.route) }
                )
            }
        }

        // Center Floating Action Button
        FloatingActionButton(
            onClick = onFabClick,
            modifier = Modifier
                .offset(y = (-20).dp)
                .size(56.dp),
            shape = CircleShape,
            containerColor = WalletPrimary,
            contentColor = Color.White,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 10.dp,
                pressedElevation = 14.dp
            )
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Quick Actions",
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
private fun RowScope.BottomNavItemView(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val tint = if (isSelected) WalletPrimary else WalletTextSecondary

    Column(
        modifier = Modifier
            .weight(1f)
            .clickable { onClick() }
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.title,
            tint = tint,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = item.title,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = tint
        )
    }
}
