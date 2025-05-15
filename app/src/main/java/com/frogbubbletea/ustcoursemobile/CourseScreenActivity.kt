package com.frogbubbletea.ustcoursemobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.frogbubbletea.ustcoursemobile.ui.screens.CourseScreen
import com.frogbubbletea.ustcoursemobile.ui.theme.USThongTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CourseScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        setContentView(R.layout.activity_course_screen)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        setContent {
            USThongTheme {
                CourseScreen()
            }
        }
    }
}