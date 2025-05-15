package com.frogbubbletea.ustcoursemobile.ui.screens

import android.content.Intent
import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.frogbubbletea.ustcoursemobile.R
import com.frogbubbletea.ustcoursemobile.StarredScreenActivity
import com.frogbubbletea.ustcoursemobile.data.Course
import com.frogbubbletea.ustcoursemobile.data.Prefix
import com.frogbubbletea.ustcoursemobile.data.PrefixType
import com.frogbubbletea.ustcoursemobile.data.Semester
import com.frogbubbletea.ustcoursemobile.data.StarredViewModel
import com.frogbubbletea.ustcoursemobile.network.ScrapingStatus
import com.frogbubbletea.ustcoursemobile.network.scrapeCourses
import com.frogbubbletea.ustcoursemobile.ui.composables.ConnectionErrorDialog
import com.frogbubbletea.ustcoursemobile.ui.composables.CourseList
import com.frogbubbletea.ustcoursemobile.ui.composables.ExploreMenu
import com.frogbubbletea.ustcoursemobile.ui.composables.LoadingIndicatorBox
import com.frogbubbletea.ustcoursemobile.ui.theme.USThongTheme
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

// Shows all courses under a certain prefix
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrefixScreen(
//    viewModel: StarredViewModel = hiltViewModel()
) {
    // Sample data
//    val semesters = sampleSemesters
//     val prefixes = samplePrefixes
//    val courses = sampleCourses

    // Variables for course data
    var prefixes: List<Prefix> by rememberSaveable { mutableStateOf(listOf()) }  // Course code prefixes
    var semesters: List<Semester> by rememberSaveable { mutableStateOf(listOf()) }  // Semesters
    var courses: List<Course> by rememberSaveable { mutableStateOf(listOf()) }  // Courses
    var error: String by rememberSaveable { mutableStateOf("") }  // Error message

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState(),
        snapAnimationSpec = null
    )

    // Starred courses
//    val starredCourses by viewModel.getStarredCourses().collectAsState(initial = emptyList())

    // State to reset scroll position on semester/prefix change
    val courseListState = rememberLazyListState()

    // Variables to control explore menu
    var showExploreMenu by rememberSaveable { mutableStateOf(false) }
    val exploreMenuState = rememberModalBottomSheetState()
    val exploreMenuScope = rememberCoroutineScope()
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

    // Current context of prefix screen activity
    val prefixScreenContext = LocalContext.current

    // Load course data
    var loadingTrigger by rememberSaveable { mutableStateOf(false) }
    var scraping by rememberSaveable { mutableStateOf(ScrapingStatus.LOADING) }
//    if (scraping == ScrapingStatus.LOADING) {
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

//            println(courses)  // DEBUG PRINT

            // Assign first prefix and latest semester after initial scrape
//            if (selectedPrefix.name == "")
//                selectedPrefix = prefixes[0]
//            if (selectedSemester.code == 0)
//                selectedSemester = semesters[0]
            selectedPrefix = scrapeResult.scrapedPrefix
            selectedSemester = scrapeResult.scrapedSemester

            // Scroll to top for changed course list
            courseListState.requestScrollToItem(0)

            scraping = ScrapingStatus.SUCCESS
        } catch (e: Exception) {
            scraping = ScrapingStatus.ERROR
            error = e.stackTraceToString()
        }
//        }
    }

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
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
//                stop = MaterialTheme.typography.titleMedium,
//                fraction = topBarCollapsedFraction
//            )
//            val topBarSubtitleTextStyle = lerp(
//                start = MaterialTheme.typography.bodyMedium,
//                stop = MaterialTheme.typography.bodySmall,
//                fraction = topBarCollapsedFraction
//            )

            TopAppBar(
                title = {
                    Surface(
                        onClick = { showExploreMenu = true},
                        color = Color.Transparent,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Column {
                                // Course code prefix
                                Text(
                                    text = selectedPrefix.name,
                                    style = MaterialTheme.typography.titleMedium,
                                )
                                // Full name of course code prefix
//                                Text(
//                                    text = "Computer Science and Engineering",
//                                    style = MaterialTheme.typography.bodySmall,
//                                )
                                // Semester
                                Text(
                                    text = selectedSemester.name,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Icon(
                                painter = painterResource(R.drawable.material_icon_dropdown),
                                contentDescription = stringResource(id = R.string.explore_icon_desc),
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        // Explore menu
        if (showExploreMenu) {
            ExploreMenu(
                semesters = semesters.toImmutableList(),
                prefixes = prefixes.toImmutableList(),
                selectedSem = selectedSemester,
                selectedPrefix = selectedPrefix,
                onSelectStarred = {
                    exploreMenuScope.launch { exploreMenuState.hide() }.invokeOnCompletion {
                        if (!exploreMenuState.isVisible) {
                            showExploreMenu = false
                        }

                        val starredIntent =
                            Intent(prefixScreenContext, StarredScreenActivity::class.java)
                        prefixScreenContext.startActivity(starredIntent)
                    }
                },
                onSelectSem = { semester ->
                    loadingTrigger = !loadingTrigger
                    selectedSemester = semester
                    exploreMenuScope.launch { exploreMenuState.hide() }.invokeOnCompletion {
                        if (!exploreMenuState.isVisible) {
                            showExploreMenu = false
                        }
                    }
                },
                onSelectPrefix = { prefix ->
                    loadingTrigger = !loadingTrigger
                    selectedPrefix = prefix
                    exploreMenuScope.launch { exploreMenuState.hide() }.invokeOnCompletion {
                        if (!exploreMenuState.isVisible) {
                            showExploreMenu = false
                        }
                    }
                },
                onDismissRequest = {
                    exploreMenuScope.launch { exploreMenuState.hide() }.invokeOnCompletion {
                        if (!exploreMenuState.isVisible) {
                            showExploreMenu = false
                        }
                    }
                },
                sheetState = exploreMenuState,
            )
        }

//        when (scraping) {
//            ScrapingStatus.LOADING -> Unit
//            ScrapingStatus.ERROR -> Unit
//            ScrapingStatus.SUCCESS -> CourseList(
//                innerPadding = innerPadding,
//                courses = courses
//            )
//        }
        if (scraping == ScrapingStatus.LOADING) {
            LoadingIndicatorBox(innerPadding)
        }

        if (scraping == ScrapingStatus.ERROR) {
//            LazyColumn(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(innerPadding)
//            ) {
//                item {
//                    Text(error)
//                }
//            }
            ConnectionErrorDialog(
                stackTrace = error,
                retryFunction = { loadingTrigger = !loadingTrigger }
            )
        }

        AnimatedVisibility(
            visible = (scraping == ScrapingStatus.SUCCESS),
            enter = slideInVertically(
                initialOffsetY = { 200 }
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { 200 }
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
                    courses = courses.toImmutableList(),
                    listState = courseListState
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
fun GreetingPreview() {
    USThongTheme {
        PrefixScreen()
    }
}