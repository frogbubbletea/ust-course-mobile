package com.frogbubbletea.ustcoursemobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.frogbubbletea.ustcoursemobile.ui.screens.PrefixScreen
import com.frogbubbletea.ustcoursemobile.ui.theme.USThongTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            USThongTheme {
//                PrefixScreen("COMP")
//                CourseScreen()
//                AppScreen()
//                NewNavigation()
                PrefixScreen()
            }
        }
    }
}