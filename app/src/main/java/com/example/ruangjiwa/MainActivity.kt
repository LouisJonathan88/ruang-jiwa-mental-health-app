package com.example.ruangjiwa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.ruangjiwa.ui.theme.RuangJiwaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RuangJiwaTheme {
                AppNavigation()
            }
        }
    }
}