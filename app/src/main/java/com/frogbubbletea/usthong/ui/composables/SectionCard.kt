package com.frogbubbletea.usthong.ui.composables

import android.graphics.drawable.Icon
import android.widget.Toast
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.frogbubbletea.usthong.R
import com.frogbubbletea.usthong.data.Quota
import com.frogbubbletea.usthong.data.ReservedQuota
import com.frogbubbletea.usthong.data.Section
import com.frogbubbletea.usthong.data.SectionSchedule
import com.frogbubbletea.usthong.data.sampleCourses
import com.frogbubbletea.usthong.ui.screens.CourseAttribute
import com.frogbubbletea.usthong.ui.theme.USThongTheme
import kotlinx.coroutines.launch

// Schedule and quota of 1 section
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectionCard(
    section: Section
) {
    // Variables to control schedule selection sheet
    var selectedScheduleIndex by remember { mutableIntStateOf(0) }
    val scheduleSheetState = rememberModalBottomSheetState()
    val scheduleSheetScope = rememberCoroutineScope()
    var scheduleSheetExpanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        shadowElevation = 1.dp,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Column (
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Experimental horizontal layout of section code and schedule selection menu
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.Bottom,
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                // Section code
//                Text(
//                    text = "L1 (1001)",
//                    style = MaterialTheme.typography.titleLarge
//                )
//
//                // Schedule selection menu
//                ScheduleSelectionMenu()
//            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Section code
                Text(
                    text = "${section.code} (${section.classNbr})",
                    style = MaterialTheme.typography.titleLarge
                )

                // Remarks
                CompositionLocalProvider(  // Remove hardcoded padding around button
                    LocalMinimumInteractiveComponentSize provides Dp.Unspecified,
                ) {
                    section.remarks.map { remark ->
                        RemarksRow(remark.key, remark.value)
                    }
                }
            }

            // Schedule selection menu
            if (section.schedules.size > 1) {
                CompositionLocalProvider(  // Remove hardcoded padding around button
                    LocalMinimumInteractiveComponentSize provides Dp.Unspecified,
                ) {
                    Surface(
                        onClick = { scheduleSheetExpanded = !scheduleSheetExpanded },
                        color = Color.Transparent,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = section.schedules[selectedScheduleIndex].effectivePeriod,
                                style = MaterialTheme.typography.bodySmall,
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

                if (scheduleSheetExpanded) {
                    ScheduleSelectionMenu(
                        schedules = section.schedules,
                        selectedScheduleIndex = selectedScheduleIndex,
                        onSelectSchedule = { i ->
                            selectedScheduleIndex = i
                            scheduleSheetScope.launch { scheduleSheetState.hide() }
                                .invokeOnCompletion {
                                    if (!scheduleSheetState.isVisible) {
                                        scheduleSheetExpanded = false
                                    }
                                }
                        },
                        onDismissRequest = {
                            scheduleSheetScope.launch { scheduleSheetState.hide() }.invokeOnCompletion {
                                if (!scheduleSheetState.isVisible) {
                                    scheduleSheetExpanded = false
                                }
                            }
                        },
                        sheetState = scheduleSheetState
                    )
                }
            }

            SectionSchedule(section.schedules[selectedScheduleIndex])

            // Quotas
            SectionQuotas(
                totalQuotas = section.totalQuota,
                reservedQuotas = section.reservedQuotas
            )
        }
    }
}

// Remarks row
@Composable
fun RemarksRow(
    short: String,
    remark: String
) {
    // Context for toast message showing remarks full text
    val context = LocalContext.current

    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.tertiaryContainer,
        onClick = { Toast.makeText(context, remark, Toast.LENGTH_SHORT).show() }
    ) {
        Text(
            text = short,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

// Schedule selection menu
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleSelectionMenu(
    // TODO: Add parameter to make it accept list of schedules
//    schedules: List<String> = listOf("02-NOV-2024 - 02-NOV-2024", "02-NOV-2024 - 02-NOV-2024", "16-NOV-2024 - 16-NOV-2024", "16-NOV-2024 - 16-NOV-2024", "30-NOV-2024 - 30-NOV-2024")
    schedules: List<SectionSchedule>,
    selectedScheduleIndex: Int,
    onSelectSchedule: (Int) -> Unit = { },
    onDismissRequest: () -> Unit = { },
    sheetState: SheetState
) {
    // Display selected schedule in collapsed menu

    CompositionLocalProvider(
        LocalMinimumInteractiveComponentSize provides Dp.Unspecified,
    ) {
        // TODO: Add date & time to each entry in bottom sheet to distinguish between schedules with the same date range
        ModalBottomSheet(
            onDismissRequest = {
                onDismissRequest()
            },
            sheetState = sheetState,
            modifier = Modifier.statusBarsPadding()
        ) {
            // Title of bottom sheet
            Text(
                text = "Select schedule",
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Add schedules to the sheet one by one
            LazyColumn {
                items(schedules.size) { i ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth(),
                        color = if (i == selectedScheduleIndex) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent,
                        onClick = { onSelectSchedule(i) }
                    ) {
                        Text(
                            text = schedules[i].effectivePeriod,
                            modifier = Modifier
                                .padding(horizontal = 32.dp, vertical = 12.dp),
                            color = if (i == selectedScheduleIndex) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

// Section schedule
@Composable
fun SectionSchedule(
    // TODO: Add parameter to make it accept time, venue, instructor, TA data
    schedule: SectionSchedule
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Time
            SectionScheduleRow(
                rowIconID = R.drawable.material_icon_calendar_clock,
                rowIconDescription = R.string.datetime_icon_desc,
                rowContent = schedule.dateTimes.joinToString("; ")
            )

            // Venue
            SectionScheduleRow(
                rowIconID = R.drawable.material_icon_location,
                rowIconDescription = R.string.venue_icon_desc,
                rowContent = schedule.venue.joinToString("; ")
            )

            // Instructor
            SectionScheduleRow(
                rowIconID = R.drawable.material_icon_person,
                rowIconDescription = R.string.inst_icon_desc,
                rowContent = schedule.instructors.joinToString("; ")
            )

            // TA
            if (schedule.teachingAssistants.size > 0) {
                SectionScheduleRow(
                    rowIconID = R.drawable.material_icon_people,
                    rowIconDescription = R.string.ta_icon_desc,
                    rowContent = schedule.teachingAssistants.joinToString("; ")
                )
            }
        }
    }
}

// A row in section schedule
@Composable
fun SectionScheduleRow(
    rowIconID: Int,
    rowIconDescription: Int,
    rowContent: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            painter = painterResource(rowIconID),
            contentDescription = stringResource(id = rowIconDescription),
            modifier = Modifier.size(12.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = rowContent,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

// Quotas of a section
@Composable
fun SectionQuotas(
    // TODO: Add parameter to make it accept quota data (total and reserved)
    totalQuotas: Quota,
    reservedQuotas: List<ReservedQuota>
) {
    val totalQuotasRow: List<Int> = listOf(totalQuotas.quota, totalQuotas.enrol, totalQuotas.avail, totalQuotas.wait)
    val quotaHeadings: List<String> = listOf("Quota", "Enrol", "Avail", "Wait")

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Headings
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(
                modifier = Modifier
                    .weight(.3f)
            )
            for (i in 0..3) {
                Text(
                    text = quotaHeadings[i],
                    modifier = Modifier.weight(.175f),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Total quotas
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Quota type
                Text(
                    text = "Total",
                    modifier = Modifier.weight(.3f),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )

                // Quota counts
                for (i in 0..3) {
                    Column(
                        modifier = Modifier.weight(.175f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = totalQuotasRow[i].toString(),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Reserved quotas
            reservedQuotas.map { reservedQuota ->
                ReservedQuotaRow(reservedQuota)
            }
        }
    }
}

// A row of reserved quotas of a section
@Composable
fun ReservedQuotaRow(
    // TODO: Add parameter to make it accept reserved quota data
    reservedQuota: ReservedQuota
) {
    val reservedQuotaDept: String = reservedQuota.dept
    val reservedQuotas: List<Int> = listOf(reservedQuota.quota, reservedQuota.enrol, reservedQuota.enrol)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Reserved department
        Text(
            text = reservedQuotaDept,
            modifier = Modifier.weight(.3f),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium
        )

        // Reserved quota counts
        for (i in 0..2) {
            Text(
                text = reservedQuotas[i].toString(),
                modifier = Modifier.weight(.175f),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
//                fontWeight = FontWeight.Medium
            )
        }

        Spacer(
            modifier = Modifier
                .width(1.dp)
                .weight(.175f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SectionCardPreview() {
    USThongTheme {
        SectionCard(sampleCourses[0].sections[0])
    }
}