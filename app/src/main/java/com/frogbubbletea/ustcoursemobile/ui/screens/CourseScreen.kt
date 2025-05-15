package com.frogbubbletea.ustcoursemobile.ui.screens

import android.content.Intent
import android.content.res.Configuration
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.frogbubbletea.ustcoursemobile.CourseScreenActivity
import com.frogbubbletea.ustcoursemobile.R
import com.frogbubbletea.ustcoursemobile.data.Course
import com.frogbubbletea.ustcoursemobile.data.MatchingRequirement
import com.frogbubbletea.ustcoursemobile.data.Prefix
import com.frogbubbletea.ustcoursemobile.data.PrefixType
import com.frogbubbletea.ustcoursemobile.data.Semester
import com.frogbubbletea.ustcoursemobile.data.StarredViewModel
import com.frogbubbletea.ustcoursemobile.data.sampleCourses
import com.frogbubbletea.ustcoursemobile.data.semesterCodeToInstance
import com.frogbubbletea.ustcoursemobile.network.ScrapeResult
import com.frogbubbletea.ustcoursemobile.network.ScrapingStatus
import com.frogbubbletea.ustcoursemobile.network.scrapeCourses
import com.frogbubbletea.ustcoursemobile.ui.composables.ConnectionErrorDialog
import com.frogbubbletea.ustcoursemobile.ui.composables.CourseNotFoundDialog
import com.frogbubbletea.ustcoursemobile.ui.composables.LoadingIndicatorBox
import com.frogbubbletea.ustcoursemobile.ui.composables.SectionCard
import com.frogbubbletea.ustcoursemobile.ui.theme.USThongTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseScreen(
    viewModel: StarredViewModel = hiltViewModel()
) {
    // Get course from the screen that launched this activity
    val activity = LocalActivity.current
    val intent = activity?.intent

    val fromPrefixName = intent?.getStringExtra("prefixName") ?: ""
    val fromPrefixType = when (intent?.getStringExtra("prefixType")) {
        "UG" -> PrefixType.UG
        "PG" -> PrefixType.PG
        else -> PrefixType.UNDEFINED
    }

    val fromPrefix = Prefix(
        name = fromPrefixName,
        type = fromPrefixType
    )

    val fromCode = intent?.getStringExtra("code") ?: ""

    val fromSemesterCode = intent?.getStringExtra("semesterCode") ?: ""
    val fromSemester = semesterCodeToInstance(fromSemesterCode.toInt())

    // Variables for course data
//    var courses: List<Course> by rememberSaveable { mutableStateOf(listOf()) }  // Courses
    var course: Course by rememberSaveable { mutableStateOf(
        Course(
            prefix = Prefix("", PrefixType.UNDEFINED),
            code = "",
            semester = Semester("", 0),
            title = "",
            units = 0,
            matching = MatchingRequirement.NONE,
            attributes = mapOf(),
            info = mapOf(),
            sections = listOf()
        )
    ) }
    var error: String by rememberSaveable { mutableStateOf("") }  // Error message

    // Starred courses
    val starredCourses by viewModel.getStarredCourses().collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()

    // Set top bar to collapse when scrolled down
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState(),
        snapAnimationSpec = null
    )

    // Load course data
    var loadingTrigger by rememberSaveable { mutableStateOf(false) }
    var scrapingStatus by rememberSaveable { mutableStateOf(ScrapingStatus.LOADING) }
    LaunchedEffect(loadingTrigger) {
        scrapingStatus = ScrapingStatus.LOADING

        try {
            // Perform course data scraping
            val scrapeResult = scrapeCourses(
                prefix = if (fromPrefixName == "") null else fromPrefix,
                semester = if (fromSemesterCode == "") null else fromSemester
            )

            // Find target course from scraped data
            val targetCourses: ImmutableList<Course> = scrapeResult.courses
                .filter { course ->
                    (course.prefix.name == fromPrefix.name) && (course.code == fromCode) && (course.semester == fromSemester)
                }
                .toImmutableList()

            // Find target course from all semesters (for course info links)
            if (targetCourses.isEmpty()) {
                val scrapedSemesters: ImmutableList<Semester> = scrapeResult.semesters.toImmutableList()
                for (s: Semester in scrapedSemesters) {
                    try {
                        val newScrapeResult: ScrapeResult = scrapeCourses(
                            prefix = if (fromPrefixName == "") null else fromPrefix,
                            semester = s
                        )
                        val newTargetCourses: ImmutableList<Course> = newScrapeResult.courses
                            .filter { c ->
                                (c.prefix.name == fromPrefix.name) && (c.code == fromCode) && (c.semester == s)
                            }
                            .toImmutableList()

                        if (newTargetCourses.isEmpty()) {
                            continue
                        } else {
                            course = newTargetCourses.first()
                            break
                        }
                    } catch (e: Exception) {
                        continue
                    }
                }

                // Display error if course is not found in all available semesters
                if (course.code == "") {
                    throw IllegalArgumentException("Course not found in all available semesters.")
                }
            } else {
                course = targetCourses.first()
            }

            scrapingStatus = ScrapingStatus.SUCCESS
        } catch (e: IllegalArgumentException) {
            scrapingStatus = ScrapingStatus.ERROR
            error = "courseNotFound"
        }
        catch (e: Exception) {
            scrapingStatus = ScrapingStatus.ERROR
            error = e.stackTraceToString()
        }
    }

    Scaffold(
        modifier = Modifier
            // Connect content's scroll position to top bar so it can be collapsed
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
            TopAppBar(
                title = {
                    Text("${course.prefix.name} ${course.code}")
                },

                // Back button
                navigationIcon = {
                    IconButton(onClick = { activity?.finish() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back_icon_desc)
                        )
                    }
                },

                actions = {
                    // Star button
                    // TODO: Implement course starring function
                    // Show different icons and actions when course is starred or not
                    if (starredCourses.filter { x -> x.id == course.prefix.name + course.code + course.semester.code.toString() }.isEmpty()) {
                        IconButton(
                            onClick = { coroutineScope.launch {
                                viewModel.starCourse(
                                    prefixName = course.prefix.name,
                                    prefixType = course.prefix.type.toString(),
                                    code = course.code,
                                    semCode = course.semester.code,
                                    semName = course.semester.name
                                )
                            } }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.material_icon_star),
                                contentDescription = stringResource(id = R.string.star_icon_desc)
                            )
                        }
                    } else {
                        IconButton(
                            onClick = { coroutineScope.launch {
                                viewModel.unstarCourse(
                                    prefixName = course.prefix.name,
                                    prefixType = course.prefix.type.toString(),
                                    code = course.code,
                                    semCode = course.semester.code,
                                    semName = course.semester.name
                                )
                            } }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.material_icon_star_filled),
                                contentDescription = stringResource(id = R.string.star_icon_desc)
                            )
                        }
                    }

                    // Dropdown menu button
                    ExternalLinksDropdown(
                        prefix = fromPrefix,
                        code = fromCode,
                        semester = fromSemester
                    )
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ),

                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        if (scrapingStatus == ScrapingStatus.LOADING) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(innerPadding)
//            ) {
//                Box(
//                    contentAlignment = Alignment.TopCenter,
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(16.dp)
//                ) {
//                    CircularProgressIndicator(
//                        modifier = Modifier
//                            .width(24.dp),
//                    )
//                }
//            }

            LoadingIndicatorBox(innerPadding)
        }

        if (scrapingStatus == ScrapingStatus.ERROR) {
//            LazyColumn(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(innerPadding)
//            ) {
//                item {
//                    Text(fromPrefix.toString())
//                }
//                item {
//                    Text(fromCode)
//                }
//                item {
//                    Text(fromSemester.toString())
//                }
//                item {
//                    Text(error)
//                }
//            }

            when (error) {
                "courseNotFound" -> CourseNotFoundDialog(
                    courseCode = fromPrefix.name + fromCode,
                    exitFunction = { activity?.finish() }
                )
                else -> ConnectionErrorDialog(
                    stackTrace = error,
                    retryFunction = { loadingTrigger = !loadingTrigger }
                )
            }
        }

        AnimatedVisibility(
            visible = (scrapingStatus == ScrapingStatus.SUCCESS),
            enter = slideInVertically(
                initialOffsetY = { 120 }
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { 120 }
            ) + fadeOut()
        ) {
            val refreshState = rememberPullToRefreshState()

            PullToRefreshBox(
                isRefreshing = scrapingStatus == ScrapingStatus.LOADING,
                onRefresh = { loadingTrigger = !loadingTrigger },
                state = refreshState,
                indicator = {
                    Indicator(
                        modifier = Modifier.align(Alignment.TopCenter),
                        isRefreshing = scrapingStatus == ScrapingStatus.LOADING,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        state = refreshState,
                    )
                },
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                LazyColumn(
//                    modifier = Modifier.padding(innerPadding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Semester and credits
                    item(key = "semCred") {
                        Text(
                            text = "${course.semester.name} • ${course.units} units",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Course title
                    item(key = "title") {
                        SelectionContainer {
                            Text(
                                text = course.title,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }

                    // Course attributes
                    item(key = "attr") {
                        CompositionLocalProvider(
                            LocalMinimumInteractiveComponentSize provides Dp.Unspecified,
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Matching requirement
                                when (course.matching) {
                                    MatchingRequirement.NONE -> Unit
                                    MatchingRequirement.TUTORIAL -> CourseAttribute(
                                        "Matching (T)",
                                        "Matching between Lecture & Tutorial required",
                                        true
                                    )

                                    MatchingRequirement.LAB -> CourseAttribute(
                                        "Matching (LA)",
                                        "Matching between Lecture & Lab required",
                                        true
                                    )
                                }

                                // Attributes
                                course.attributes.map { attr ->
                                    CourseAttribute(attr.key, attr.value)
                                }
                            }
                        }
                    }

                    // Course info
                    item(key = "info") {
                        CourseInfoContainer(
                            courseInfo = course.info.toImmutableMap(),
                            fromSemesterCode = fromSemesterCode
                        )
                    }

                    // Sections
                    items(
                        items = course.sections,
                        key = { it.classNbr }
                    ) { section ->
                        SectionCard(section)
                    }

                    // Reserve space for system navbar
                    item(key = "spacer") {
                        Spacer(
                            modifier = Modifier
                                .height(0.dp)
                                .navigationBarsPadding()
                        )
                    }
                }
            }
        }
    }
}

// Dropdown menu containing external links
@Composable
fun ExternalLinksDropdown(
    prefix: Prefix,
    code: String,
    semester: Semester
) {
    var expanded by remember { mutableStateOf(false) }

    // Handle external links
    val uriHandler = LocalUriHandler.current

    Box {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Default.MoreVert, contentDescription = "More options")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = "USTSPACE Reviews",
                        fontWeight = FontWeight.Normal,
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                onClick = {
                    uriHandler.openUri("https://ust.space/review/${prefix.name}${code}")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Open in Class Schedule & Quota",
                        fontWeight = FontWeight.Normal,
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                onClick = {
                    uriHandler.openUri("https://w5.ab.ust.hk/wcq/cgi-bin/${semester.code}/subject/${prefix.name}#${prefix.name}${code}")
                    expanded = false
                }
            )
        }
    }
}

// Container showing a course attribute code
@Composable
fun CourseAttribute(
    short: String,
    attr: String,
    alert: Boolean = false
) {
    // Context for toast message showing attribute full text
    val context = LocalContext.current

    // Highlight attribute for important ones (matching required)
    val attrBgColor = if (alert) {
        MaterialTheme.colorScheme.tertiaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceContainerHigh
    }
    val attrTextColor = if (alert) {
        MaterialTheme.colorScheme.onTertiaryContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        shape = MaterialTheme.shapes.small,
        color = attrBgColor,
        onClick = { Toast.makeText(context, attr, Toast.LENGTH_SHORT).show() }  // Show full text of attribute
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        ) {
            Text(
                text = short,
                style = MaterialTheme.typography.labelMedium,
//                fontWeight = FontWeight.Medium,
                color = attrTextColor
            )
        }
    }
}

// Surface containing course info of a course
@Composable
fun CourseInfoContainer(
    courseInfo: ImmutableMap<String, String>,
    fromSemesterCode: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        SelectionContainer {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                courseInfo.map { info ->
                    Column {
                        // Course info name
                        Text(
                            text = info.key,
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )

                        // Course info content
//                        Text(
//                            text = info.value,
//                            style = MaterialTheme.typography.bodySmall
//                        )

                        // Link to courses mentioned in course info
                        val infoCourseCodeRegex: Regex = Regex("[A-Z]{4} \\d{4}[A-Z]?")
                        val infoCourseCodeMatches: ImmutableList<String> = infoCourseCodeRegex.findAll(info.value)
                            .map { x -> x.value }
                            .toImmutableList()
                        val infoSurroundingTexts: ImmutableList<String> = info.value
                            .split(infoCourseCodeRegex)
                            .toImmutableList()

                        Text(
                            text = buildAnnotatedString {
                                val context = LocalContext.current

                                // Add surrounding text, then link, vice versa
                                infoCourseCodeMatches.mapIndexed { idx, courseCode ->
                                    append(infoSurroundingTexts[idx])
                                    withLink(
                                        LinkAnnotation.Clickable(
                                            tag = courseCode,
                                            styles = TextLinkStyles(
                                                style = SpanStyle(
                                                    color = MaterialTheme.colorScheme.primary,
                                                    textDecoration = TextDecoration.Underline
                                                )
                                            )
                                        ) {
                                            val courseCodeLinkIntent = Intent(context, CourseScreenActivity::class.java)
                                            courseCodeLinkIntent.putExtra("prefixName", courseCode.substring(0, 4))
                                            courseCodeLinkIntent.putExtra("prefixType", "")  // Prefix type not needed
                                            courseCodeLinkIntent.putExtra("code", courseCode.substring(5))
                                            courseCodeLinkIntent.putExtra("semesterCode", fromSemesterCode)
                                            context.startActivity(courseCodeLinkIntent)
                                        }
                                    ) {
                                        append(courseCode)
                                    }
                                }

                                // Add last surrounding text
                                append(infoSurroundingTexts[infoSurroundingTexts.size - 1])
                            },
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
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
fun CourseInfoPreview() {
    USThongTheme {
        CourseInfoContainer(sampleCourses[0].info.toImmutableMap(), "2440")
    }
}