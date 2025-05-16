package com.frogbubbletea.ustcoursemobile.ui.screens

import android.content.res.Configuration
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.frogbubbletea.ustcoursemobile.R
import com.frogbubbletea.ustcoursemobile.data.Course
import com.frogbubbletea.ustcoursemobile.data.CourseEntity
import com.frogbubbletea.ustcoursemobile.data.Prefix
import com.frogbubbletea.ustcoursemobile.data.PrefixType
import com.frogbubbletea.ustcoursemobile.data.Semester
import com.frogbubbletea.ustcoursemobile.data.StarredViewModel
import com.frogbubbletea.ustcoursemobile.data.sampleCourses
import com.frogbubbletea.ustcoursemobile.data.semesterCodeToInstance
import com.frogbubbletea.ustcoursemobile.network.ScrapingStatus
import com.frogbubbletea.ustcoursemobile.network.scrapeCourses
import com.frogbubbletea.ustcoursemobile.ui.composables.ConnectionErrorDialog
import com.frogbubbletea.ustcoursemobile.ui.composables.CourseList
import com.frogbubbletea.ustcoursemobile.ui.composables.LoadingIndicatorBox
import com.frogbubbletea.ustcoursemobile.ui.theme.USThongTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

// Shows all courses starred by the user
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StarredScreen(
    viewModel: StarredViewModel = hiltViewModel()
) {
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

    // Starred courses
    val starredCourses by viewModel.getStarredCourses().collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()

    // Load course data
    var loadingTrigger by rememberSaveable { mutableStateOf(false) }
    var scraping by rememberSaveable { mutableStateOf(ScrapingStatus.LOADING) }
    var starredCourseObjects: List<Course> by rememberSaveable { mutableStateOf(listOf()) }
    var notFoundCourseEntities: List<CourseEntity> by rememberSaveable { mutableStateOf(listOf()) }
    var showNotFoundCoursesDialog by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(loadingTrigger, starredCourses) {
        scraping = ScrapingStatus.LOADING

        try {
            // Iterate over unique semesters and prefixes
            val starredCoursesMap = starredCourses.groupBy { Pair(it.semCode, it.prefixName) }

            starredCourseObjects = starredCoursesMap.map { entry ->
                try {
                    // Perform course data scraping
                    val scrapeResult = scrapeCourses(
                        prefix = Prefix(entry.key.second, PrefixType.UNDEFINED),
                        semester = semesterCodeToInstance(entry.key.first)
                    )

                    // Unpack scraped data
                    prefixes = scrapeResult.prefixes
                    semesters = scrapeResult.semesters
                    courses = scrapeResult.courses

                    // Find starred courses from scraped data
                    scrapeResult.courses
                        .filter { course ->
                            CourseEntity(
                                id = course.prefix.name + course.code + course.semester.code.toString(),
                                prefixName = course.prefix.name,
                                prefixType = course.prefix.type.toString(),
                                code = course.code,
                                semCode = course.semester.code,
                                semName = course.semester.name
                            ) in entry.value
                        }
                        .toImmutableList()
                } catch (e: Exception) {
                    listOf()
                }
            }.flatten().sortedWith(compareBy({ it.prefix.name }, { it.code }, { -it.semester.code }))

            // Look for starred courses that are not found in the scraped data
            val foundStarredCourseEntities = starredCourseObjects.map { course ->
                CourseEntity(
                    id = course.prefix.name + course.code + course.semester.code.toString(),
                    prefixName = course.prefix.name,
                    prefixType = course.prefix.type.toString(),
                    code = course.code,
                    semCode = course.semester.code,
                    semName = course.semester.name
                )
            }.toImmutableList()
            notFoundCourseEntities = starredCourses.filter { ce -> ce !in foundStarredCourseEntities}
            showNotFoundCoursesDialog = notFoundCourseEntities.isNotEmpty()

            // Perform course data scraping
//            val scrapeResult = scrapeCourses(
//                prefix = if (selectedPrefix.name == "") null else selectedPrefix,
//                semester = if (selectedSemester.code == 0) null else selectedSemester
//            )
//
//            // Unpack scraped data
//            prefixes = scrapeResult.prefixes
//            semesters = scrapeResult.semesters
//            courses = scrapeResult.courses
//
//
//            // Assign first prefix and latest semester after initial scrape
//            selectedPrefix = scrapeResult.scrapedPrefix
//            selectedSemester = scrapeResult.scrapedSemester

            scraping = ScrapingStatus.SUCCESS
        } catch (e: Exception) {
            scraping = ScrapingStatus.ERROR
            error = e.stackTraceToString()
        }
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
        // Course not found dialog
        if (showNotFoundCoursesDialog) {
            Dialog(
                onDismissRequest = { showNotFoundCoursesDialog = false },
            ) {
                Surface(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight(),
                    shape = MaterialTheme.shapes.extraLarge,
                    tonalElevation = AlertDialogDefaults.TonalElevation
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.material_icon_info),
                            contentDescription = stringResource(id = R.string.info_icon_desc),
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.secondary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        val titleText: String = when (notFoundCourseEntities.size) {
                            1 -> "Unstar unavailable course?"
                            else -> "Unstar unavailable courses?"
                        }

                        Text(
                            text = titleText,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        val supportingText: String = when (notFoundCourseEntities.size) {
                            1 -> "The following starred course could not be found on HKUST Class Schedule & Quota. It may be temporarily unavailable or permanently removed."
                            else -> "The following starred courses could not be found on HKUST Class Schedule & Quota. They may be temporarily unavailable or permanently removed."
                        }

                        Text(
                            supportingText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outline,
                            thickness = 1.dp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 200.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            userScrollEnabled = true
                        ) {
                            items(
                                items = notFoundCourseEntities,
                                key = { it.id }
                            ) { c ->
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.Start)
                                ) {
                                    Text(
                                        text = "${c.prefixName} ${c.code}",
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = c.semName,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outline,
                            thickness = 1.dp
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.align(Alignment.End),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            TextButton(
                                onClick = { showNotFoundCoursesDialog = false }
                            ) {
                                Text(
                                    text = "No",
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            TextButton(
                                onClick = {
                                    coroutineScope.launch {
                                        notFoundCourseEntities.map {
                                            ce -> viewModel.unstarCourse(
                                                prefixName = ce.prefixName,
                                                prefixType = ce.prefixType,
                                                code = ce.code,
                                                semCode = ce.semCode,
                                                semName = ce.semName
                                            )
                                        }
                                    }
                                    showNotFoundCoursesDialog = false
                                }
                            ) {
                                Text(
                                    text = "Yes",
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }

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
                    courses = starredCourseObjects.toImmutableList(),
                    showSemester = true
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