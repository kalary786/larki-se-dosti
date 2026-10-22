package com.larkisedosti.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.larkisedosti.app.ui.theme.LarkiSeDostiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LarkiSeDostiTheme(darkTheme = true) {
                LarkiApp()
            }
        }
    }
}
