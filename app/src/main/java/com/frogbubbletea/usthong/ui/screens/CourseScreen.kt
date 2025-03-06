package com.frogbubbletea.usthong.ui.screens

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent.getIntent
import android.content.Intent.parseUri
import android.content.res.Configuration
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frogbubbletea.usthong.R
import com.frogbubbletea.usthong.data.MatchingRequirement
import com.frogbubbletea.usthong.data.sampleCourses
import com.frogbubbletea.usthong.ui.composables.CourseCard
import com.frogbubbletea.usthong.ui.composables.SectionCard
import com.frogbubbletea.usthong.ui.theme.USThongTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseScreen() {
    // Get course from the screen that launched this activity
    val activity = LocalActivity.current;
    val intent = activity?.intent;
    val fromPrefix = intent?.getStringExtra("prefix") ?: ""
    val fromCode = intent?.getStringExtra("code") ?: ""
    val course = sampleCourses.filter({ (it.prefix == fromPrefix) && (it.code == fromCode) })[0]

    // Set top bar to collapse when scrolled down
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState(),
        snapAnimationSpec = null
    )

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
                    Text("${course.prefix} ${course.code}")
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
                    IconButton(
                        onClick = { }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.material_icon_star),
                            contentDescription = stringResource(id = R.string.star_icon_desc)
                        )
                    }

                    // Dropdown menu button
                    ExternalLinksDropdown()
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
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Course title
            item {
                SelectionContainer {
                    Text(
                        text = course.title,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            // Course attributes
            item {
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
            item {
                CourseInfoContainer(course.info)
            }

            // Sections
            items(course.sections) { section ->
                SectionCard(section)
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
}

// Dropdown menu containing external links
@Composable
fun ExternalLinksDropdown() {
    var expanded by remember { mutableStateOf(false) }
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
                onClick = { expanded = false }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Open in Class Schedule & Quota",
                        fontWeight = FontWeight.Normal,
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                onClick = { expanded = false }
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
                style = MaterialTheme.typography.bodySmall,
//                fontWeight = FontWeight.Medium,
                color = attrTextColor
            )
        }
    }
}

// Surface containing course info of a course
@Composable
fun CourseInfoContainer(
    courseInfo: Map<String, String>
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
                        Text(
                            text = info.key,
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = info.value,
                            style = MaterialTheme.typography.bodySmall,
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
        CourseInfoContainer(sampleCourses[0].info)
    }
}