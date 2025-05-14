package com.frogbubbletea.ustcoursemobile.ui.composables

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.frogbubbletea.ustcoursemobile.CourseScreenActivity
import com.frogbubbletea.ustcoursemobile.R
import com.frogbubbletea.ustcoursemobile.data.Course
import com.frogbubbletea.ustcoursemobile.data.sampleCourses
import com.frogbubbletea.ustcoursemobile.ui.theme.USThongTheme
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

// Card showing info of a course
@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun CourseCard(
    course: Course
) {
    // Variables for section selection bottom sheet
    val sections = course.sections.toImmutableList()
    val sectionSheetState = rememberModalBottomSheetState()
    val sectionSheetScope = rememberCoroutineScope()
    var showSectionSheet by remember { mutableStateOf(false) }
    var selectedSection by remember { mutableStateOf(sections[0]) }

    LaunchedEffect(sections) {
        selectedSection = sections[0]
    }

    val context = LocalContext.current

    // Handle external links
    val uriHandler = LocalUriHandler.current

    Surface(
//        onClick = onClick,
        onClick = {
            val courseCardIntent = Intent(context, CourseScreenActivity::class.java)
            courseCardIntent.putExtra("prefixName", course.prefix.name)
            courseCardIntent.putExtra("prefixType", course.prefix.type.toString())
            courseCardIntent.putExtra("code", course.code)
            courseCardIntent.putExtra("semesterCode", course.semester.code.toString())
            context.startActivity(courseCardIntent)
        },
        modifier = Modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        shadowElevation = 1.dp,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Column (
            modifier = Modifier
                .padding(16.dp)
        ) {
            // Course code
            Text(
                text = "${course.prefix.name} ${course.code}",
                style = MaterialTheme.typography.titleLarge
            )

            // Course title
            Text(
                text = course.title,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Quota of one selected section (total)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Display of selected section
                CompositionLocalProvider(
                    LocalMinimumInteractiveComponentSize provides Dp.Unspecified,
                ) {
                    Surface(
                        onClick = { showSectionSheet = true },
                        color = Color.Transparent,
                        modifier = Modifier.weight(.3f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${selectedSection.code} (${selectedSection.classNbr})",
                                color = MaterialTheme.colorScheme.primary
                            )
                            Icon(
                                painter = painterResource(R.drawable.material_icon_dropdown),
                                contentDescription = stringResource(id = R.string.sections_icon_desc),
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                // Section selection bottom sheet
                if (showSectionSheet) {
                    ModalBottomSheet(
                        onDismissRequest = {
                            showSectionSheet = false
                        },
                        sheetState = sectionSheetState,
                        modifier = Modifier.statusBarsPadding()
                    ) {
                        // Title of section selection bottom sheet
                        Text(
                            text = "Select section",
                            modifier = Modifier
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Add sections to the sheet one by one
                        LazyColumn {
                            items(
                                items = sections,
                                key = { it.classNbr }
                            ) { section ->
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    color = if (section == selectedSection) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent,
                                    onClick = {
                                        selectedSection = section
                                        sectionSheetScope.launch { sectionSheetState.hide() }.invokeOnCompletion {
                                            if (!sectionSheetState.isVisible) {
                                                showSectionSheet = false
                                            }
                                        }
                                    }
                                ) {
                                    Text(
                                        text = "${section.code} (${section.classNbr})",
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp, vertical = 12.dp),
                                        color = if (section == selectedSection) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }

                // "Quota" column
                Column(
                    modifier = Modifier.weight(.175f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Quota",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = selectedSection.totalQuota.quota.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // "Enrol" column
                Column(
                    modifier = Modifier.weight(.175f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Enrol",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = selectedSection.totalQuota.enrol.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // "Avail" column
                Column(
                    modifier = Modifier.weight(.175f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Avail",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = selectedSection.totalQuota.avail.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // "Wait" column
                Column(
                    modifier = Modifier.weight(.175f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Wait",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = selectedSection.totalQuota.wait.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            CompositionLocalProvider(  // Remove hardcoded padding around button
                LocalMinimumInteractiveComponentSize provides Dp.Unspecified,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Number of sections
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = MaterialTheme.colorScheme.surfaceContainerHigh
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.material_icon_list),
                                contentDescription = stringResource(id = R.string.sections_icon_desc),
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                text = if (sections.size == 1) "${sections.size} section" else "${sections.size} sections",
                                style = MaterialTheme.typography.labelMedium,
//                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Number of units
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = MaterialTheme.colorScheme.surfaceContainerHigh
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.material_icon_units),
                                contentDescription = stringResource(id = R.string.units_icon_desc),
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                text = if (course.units == 1) "${course.units} unit" else "${course.units} units",
                                style = MaterialTheme.typography.labelMedium,
//                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Star button
                    // TODO: Implement course starring function
//                    IconButton(
//                        modifier = Modifier.
//                        then(Modifier.size(28.dp)),
//                        onClick = { }
//                    ) {
//                        Icon(
//                            painter = painterResource(R.drawable.material_icon_star),
//                            contentDescription = stringResource(id = R.string.star_icon_desc),
//                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
//                            modifier = Modifier.size(24.dp)
//                        )
//                    }

                    // Open in Class Schedule & Quota button
                    IconButton(
                        modifier = Modifier.
                        then(Modifier.size(28.dp)),
                        onClick = { uriHandler.openUri("https://w5.ab.ust.hk/wcq/cgi-bin/${course.semester.code}/subject/${course.prefix.name}#${course.prefix.name}${course.code}") }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.material_icon_open_in_browser),
                            contentDescription = stringResource(id = R.string.open_in_ust_icon_desc),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun CourseCardPreview() {
    USThongTheme {
        CourseCard(sampleCourses[0])
    }
}