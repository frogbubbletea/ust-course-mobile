package com.frogbubbletea.usthong.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.lerp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.frogbubbletea.usthong.R
import com.frogbubbletea.usthong.ui.composables.CourseList
import com.frogbubbletea.usthong.ui.theme.USThongTheme

// Shows all courses starred by the user
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StarredScreen(
    onNavigateToCourse: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState(),
        snapAnimationSpec = null
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = WindowInsets(bottom = 0.dp),
        topBar = {
            // Shrink top bar title as user scrolls down
//            val topBarCollapsedFraction = scrollBehavior.state.collapsedFraction
//            val topBarTitleTextStyle = lerp(
//                start = MaterialTheme.typography.headlineMedium,
//                stop = MaterialTheme.typography.titleLarge,
//                fraction = topBarCollapsedFraction
//            )

            TopAppBar(
                title = {
                    Text(
                        text = "Starred",
//                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                actions = {
                    // Search button
                    IconButton(
                        onClick = { }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.material_icon_search),
                            contentDescription = stringResource(id = R.string.search_icon_desc)
                        )
                    }

                    // Sort button
                    IconButton(
                        onClick = { }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.material_icon_sort),
                            contentDescription = stringResource(id = R.string.sort_icon_desc)
                        )
                    }
                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.surface,
//                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
//                ),
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        CourseList(innerPadding)
    }
}

@Preview(showBackground = true)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun StarredPreview() {
    USThongTheme {
        StarredScreen()
    }
}