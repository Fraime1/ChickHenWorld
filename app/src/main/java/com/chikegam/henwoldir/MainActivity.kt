package com.chikegam.henwoldir

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.chikegam.henwoldir.ui.navigation.MainScreen
import com.chikegam.henwoldir.ui.theme.ChickHenWorldTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChickHenWorldTheme {
                MainScreen()
            }
        }
    }
}