package com.frogbubbletea.usthong.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.frogbubbletea.usthong.ui.composables.CourseCard
import com.frogbubbletea.usthong.ui.theme.USThongTheme

// Shows all courses under `prefix`
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrefixScreen(prefix: String) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            // Shrink top bar title as user scrolls down
            val topBarCollapsedFraction = scrollBehavior.state.collapsedFraction
            val topBarTitleTextStyle = lerp(
                start = MaterialTheme.typography.headlineLarge,
                stop = MaterialTheme.typography.titleMedium,
                fraction = topBarCollapsedFraction
            )
            val topBarSubtitleTextStyle = lerp(
                start = MaterialTheme.typography.bodyMedium,
                stop = MaterialTheme.typography.bodySmall,
                fraction = topBarCollapsedFraction
            )

            LargeTopAppBar(
                title = {
                    Column(
                        modifier = Modifier.padding(vertical = 16.dp)
                    ) {
                        // Course code prefix
                        Text(
                            text = prefix,
                            style = topBarTitleTextStyle
                        )
                        // Full name of course code prefix
                        Text(
                            text = "Computer Science and Engineering",
                            style = topBarSubtitleTextStyle
                        )
                    }
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(40) { index ->
                CourseCard()
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun GreetingPreview() {
    USThongTheme {
        PrefixScreen("COMP")
    }
}