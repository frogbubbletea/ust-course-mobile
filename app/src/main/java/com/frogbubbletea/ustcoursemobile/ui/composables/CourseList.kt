package com.frogbubbletea.ustcoursemobile.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.frogbubbletea.ustcoursemobile.data.Course
import kotlinx.collections.immutable.ImmutableList

// Format a list of courses into a `LazyColumn` of  `CourseCard`s
@Composable
fun CourseList(
    innerPadding: PaddingValues,
    courses: ImmutableList<Course>,
    showSemester: Boolean = false,
    listState: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = Modifier
//            .padding(innerPadding)
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        state = listState
    ) {
        items(
            items = courses,
            key = { it.prefix.name + it.code + it.semester.code.toString() }
        ) { course ->
            CourseCard(
                course = course,
                showSemester = showSemester
            )
        }

        // Reserve space for system navbar
        item(
            key = "spacer"
        ) {
            Spacer(
                modifier = Modifier
                    .height(0.dp)
                    .navigationBarsPadding()
            )
        }
    }
}
