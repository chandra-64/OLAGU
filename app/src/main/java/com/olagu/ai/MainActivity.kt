package com.olagu.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.olagu.ai.ui.screens.ChatScreen
import com.olagu.ai.ui.theme.OLAGUTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OLAGUTheme {
                ChatScreen()
            }
        }
    }
}
