package com.frogbubbletea.usthong.ui.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.frogbubbletea.usthong.R
import com.frogbubbletea.usthong.ui.theme.USThongTheme
import kotlinx.coroutines.launch

// Card showing info of a course
@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun CourseCard() {
    // Variables for section selection bottom sheet
    val sections = listOf("L1 (1001)", "L2 (1002)", "L3 (1003)", "L4 (1004)", "L5 (1005)", "L06 (1006)", "L07 (1007)", "L08 (1008)", "L09 (1009)", "L10 (1010)", "L11 (1011)", "T01B (1234)", "T02C (1145)", "T05A (3555)")
    val sectionSheetState = rememberModalBottomSheetState()
    val sectionSheetScope = rememberCoroutineScope()
    var showSectionSheet by remember { mutableStateOf(false) }
    var selectedSection by remember { mutableStateOf(sections[0]) }

    //

    Surface(
        onClick = { },
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
                text = "ACCT 1010",
                style = MaterialTheme.typography.titleLarge
            )

            // Course title
            Text(
                text = "Accounting, Business and Society",
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
                    LocalMinimumInteractiveComponentEnforcement provides false,
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
                                text = selectedSection,
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
                        sheetState = sectionSheetState
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
                            items(sections) { section ->
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
                                        text = section,
                                        modifier = Modifier
                                            .padding(horizontal = 32.dp, vertical = 12.dp),
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
                        text = "120",
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
                        text = "101",
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
                        text = "19",
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
                        text = "0",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            CompositionLocalProvider(  // Remove hardcoded padding around button
                LocalMinimumInteractiveComponentEnforcement provides false,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Number of sections
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        onClick = { }
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.AutoMirrored.Outlined.List,
                                contentDescription = stringResource(id = R.string.sections_icon_desc),
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                text = "9 sections",
                                style = MaterialTheme.typography.bodySmall,
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
                                text = "3 units",
                                style = MaterialTheme.typography.bodySmall,
//                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Star button
                    IconButton(
                        modifier = Modifier.
                        then(Modifier.size(28.dp)),
                        onClick = { }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.material_icon_star),
                            contentDescription = stringResource(id = R.string.star_icon_desc),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Open in Class Schedule & Quota button
                    IconButton(
                        modifier = Modifier.
                        then(Modifier.size(28.dp)),
                        onClick = { }
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
        CourseCard()
    }
}