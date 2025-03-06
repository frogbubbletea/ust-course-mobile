package com.frogbubbletea.usthong.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Format a list of courses into a `LazyColumn` of  `CourseCard`s
@Composable
fun CourseList(
    innerPadding: PaddingValues
    // TODO: Add parameter to make it accept a list of course data
) {
    LazyColumn(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(40) { index ->
            CourseCard()
        }

        // Reserve space for system navbar
        item {
            Spacer(
                modifier = Modifier
                    .height(0.dp)
                    .navigationBarsPadding()
            )
        }
    }
}
