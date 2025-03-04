package com.frogbubbletea.usthong.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Format a list of courses into a `LazyColumn` of  `CourseCard`s
@Composable
fun CourseList(
    innerPadding: PaddingValues,
    onCourseCardClick: () -> Unit = {}
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
            CourseCard(onClick = onCourseCardClick)
        }
    }
}
