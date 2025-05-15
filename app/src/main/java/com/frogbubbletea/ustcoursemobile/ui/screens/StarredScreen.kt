package com.frogbubbletea.ustcoursemobile.ui.screens

import android.content.res.Configuration
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.frogbubbletea.ustcoursemobile.R
import com.frogbubbletea.ustcoursemobile.data.Course
import com.frogbubbletea.ustcoursemobile.data.Prefix
import com.frogbubbletea.ustcoursemobile.data.PrefixType
import com.frogbubbletea.ustcoursemobile.data.Semester
import com.frogbubbletea.ustcoursemobile.data.sampleCourses
import com.frogbubbletea.ustcoursemobile.network.ScrapingStatus
import com.frogbubbletea.ustcoursemobile.network.scrapeCourses
import com.frogbubbletea.ustcoursemobile.ui.composables.ConnectionErrorDialog
import com.frogbubbletea.ustcoursemobile.ui.composables.CourseList
import com.frogbubbletea.ustcoursemobile.ui.composables.LoadingIndicatorBox
import com.frogbubbletea.ustcoursemobile.ui.theme.USThongTheme
import kotlinx.collections.immutable.toImmutableList

// Shows all courses starred by the user
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StarredScreen() {
    val activity = LocalActivity.current

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState(),
        snapAnimationSpec = null
    )

    // Variables for course data
    var prefixes: List<Prefix> by rememberSaveable { mutableStateOf(listOf()) }  // Course code prefixes
    var semesters: List<Semester> by rememberSaveable { mutableStateOf(listOf()) }  // Semesters
    var courses: List<Course> by rememberSaveable { mutableStateOf(listOf()) }  // Courses
    var error: String by rememberSaveable { mutableStateOf("") }  // Error message

    var selectedSemester by rememberSaveable { mutableStateOf(
        Semester(
            name = "",
            code = 0
        )
    ) }
    var selectedPrefix by rememberSaveable { mutableStateOf(
        Prefix(
            name = "",
            type = PrefixType.UNDEFINED,
        )
    ) }

    // Load course data
    var loadingTrigger by rememberSaveable { mutableStateOf(false) }
    var scraping by rememberSaveable { mutableStateOf(ScrapingStatus.LOADING) }

    LaunchedEffect(loadingTrigger) {
        scraping = ScrapingStatus.LOADING

        try {
            // Perform course data scraping
            val scrapeResult = scrapeCourses(
                prefix = if (selectedPrefix.name == "") null else selectedPrefix,
                semester = if (selectedSemester.code == 0) null else selectedSemester
            )

            // Unpack scraped data
            prefixes = scrapeResult.prefixes
            semesters = scrapeResult.semesters
            courses = scrapeResult.courses


            // Assign first prefix and latest semester after initial scrape
            selectedPrefix = scrapeResult.scrapedPrefix
            selectedSemester = scrapeResult.scrapedSemester

            scraping = ScrapingStatus.SUCCESS
        } catch (e: Exception) {
            scraping = ScrapingStatus.ERROR
            error = e.stackTraceToString()
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
            // Make window avoid overlapping with display cutout in landscape mode
            .then(
                if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE)
                    Modifier.windowInsetsPadding(WindowInsets.displayCutout)
                else
                    Modifier
            ),
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
                        text = "Starred courses",
//                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { activity?.finish() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back_icon_desc)
                        )
                    }
                },
                actions = {
                    // Search button
//                    IconButton(
//                        onClick = { }
//                    ) {
//                        Icon(
//                            painter = painterResource(R.drawable.material_icon_search),
//                            contentDescription = stringResource(id = R.string.search_icon_desc)
//                        )
//                    }

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
        if (scraping == ScrapingStatus.LOADING) {
            LoadingIndicatorBox(innerPadding)
        }

        if (scraping == ScrapingStatus.ERROR) {
            ConnectionErrorDialog(
                stackTrace = error,
                retryFunction = { loadingTrigger = !loadingTrigger }
            )
        }

        AnimatedVisibility(
            visible = (scraping == ScrapingStatus.SUCCESS),
            enter = slideInVertically(
                initialOffsetY = { 120 }
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { 120 }
            ) + fadeOut()
        ) {
            val refreshState = rememberPullToRefreshState()

            PullToRefreshBox(
                isRefreshing = scraping == ScrapingStatus.LOADING,
                onRefresh = { loadingTrigger = !loadingTrigger },
                state = refreshState,
                indicator = {
                    Indicator(
                        modifier = Modifier.align(Alignment.TopCenter),
                        isRefreshing = scraping == ScrapingStatus.LOADING,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        state = refreshState,
                    )
                },
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                CourseList(
                    innerPadding = innerPadding,
                    courses = courses.toImmutableList()
                )
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
fun StarredPreview() {
    USThongTheme {
        StarredScreen()
    }
}