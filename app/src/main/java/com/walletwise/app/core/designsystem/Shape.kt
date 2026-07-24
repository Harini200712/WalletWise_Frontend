package com.walletwise.app.core.designsystem

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val WalletShapes = Shapes(
    small = RoundedCornerShape(18.dp),  // TextField
    medium = RoundedCornerShape(24.dp), // Cards
    large = RoundedCornerShape(28.dp),  // Buttons & Bottom Sheet
    extraLarge = RoundedCornerShape(32.dp)
)

val CardShape = RoundedCornerShape(24.dp)
val ButtonShape = RoundedCornerShape(28.dp)
val BottomSheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
val TextFieldShape = RoundedCornerShape(18.dp)
val ChipShape = RoundedCornerShape(14.dp)
