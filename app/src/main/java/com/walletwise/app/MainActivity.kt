package com.walletwise.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.walletwise.app.core.designsystem.WalletBackground
import com.walletwise.app.core.designsystem.WalletWiseTheme
import com.walletwise.app.navigation.WalletWiseNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WalletWiseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = WalletBackground
                ) {
                    WalletWiseNavGraph()
                }
            }
        }
    }
}
