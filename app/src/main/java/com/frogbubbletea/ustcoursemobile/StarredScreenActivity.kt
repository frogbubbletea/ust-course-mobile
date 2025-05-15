package com.frogbubbletea.ustcoursemobile

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.frogbubbletea.ustcoursemobile.ui.screens.StarredScreen
import com.frogbubbletea.ustcoursemobile.ui.theme.USThongTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StarredScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        setContentView(R.layout.activity_starred_screen)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        setContent {
            USThongTheme {
                StarredScreen()
            }
        }
    }
}