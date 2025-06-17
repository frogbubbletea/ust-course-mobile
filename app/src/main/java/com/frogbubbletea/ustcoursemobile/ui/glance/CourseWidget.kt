package com.frogbubbletea.ustcoursemobile.ui.glance

import android.content.Context
import android.content.Intent
import androidx.collection.intFloatMapOf
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.ColorFilter.Companion.tint
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalSize
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.components.CircleIconButton
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.frogbubbletea.ustcoursemobile.CourseScreenActivity
import com.frogbubbletea.ustcoursemobile.R
import com.frogbubbletea.ustcoursemobile.data.glanceData.WidgetSectionEntity
import com.frogbubbletea.ustcoursemobile.data.glanceData.WidgetSectionRepository

class CourseWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = CourseWidget()
}

// TODO: Make widget display real course data
class CourseWidget : GlanceAppWidget() {
    // Sizes here assume 5x5 home screen grid
    // Width 2 in 4x5 is treated as width 3 in 5x5
    companion object {
        private val TWO_TWO = DpSize(120.dp, 80.dp)
        private val THREE_TWO = DpSize(160.dp, 80.dp)
        private val FOUR_TWO = DpSize(250.dp, 80.dp)
        private val TWO_THREE = DpSize(120.dp, 250.dp)
        private val THREE_THREE = DpSize(160.dp, 250.dp)
        private val FOUR_THREE = DpSize(250.dp, 250.dp)
    }

    override val sizeMode = SizeMode.Responsive(
        setOf(
            TWO_TWO,
            THREE_TWO,
            FOUR_TWO,
            TWO_THREE,
            THREE_THREE,
            FOUR_THREE
        )
    )

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val repo = WidgetSectionRepository.get(context)
        provideContent {
            GlanceTheme {
                MyContent(context, repo)
            }
        }
    }

    @Composable
    private fun MyContent(context: Context, repo: WidgetSectionRepository) {
        // Get sections
        val sectionsState = repo.loadSections().collectAsState(initial = emptyList())
        val sections = sectionsState.value
        // TODO: Get page number from preferences datastore
        val pageNumber = 0
        // Get section to be displayed in the widget
        val sectionToDisplay = sections.getOrElse(
            pageNumber,
            {
                WidgetSectionEntity(
                    classNbr = 0,
                    sectionCode = "",
                    quota = 0,
                    enrol = 0,
                    avail = 0,
                    wait = 0,
                    hasReserved = false
                )
            }
        )

        // Get current widget size
        val size = LocalSize.current

        // Use material theme text styles
        val headlineMedium = TextStyle(
            color = GlanceTheme.colors.onSurface,
            fontSize = 28.sp,
            fontWeight = FontWeight.Normal
        )
        val headlineSmall = TextStyle(
            color = GlanceTheme.colors.onSurface,
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal
        )
        val titleLarge = TextStyle(
            color = GlanceTheme.colors.onSurface,
            fontSize = 22.sp,
            fontWeight = FontWeight.Normal,
        )
        val titleMedium = TextStyle(
            color = GlanceTheme.colors.onSurface,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        val bodyLarge = TextStyle(
            color = GlanceTheme.colors.onSurface,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )
        val bodyMedium = TextStyle(
            color = GlanceTheme.colors.onSurface,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
        )
        val labelMedium = TextStyle(
            color = GlanceTheme.colors.onSurface,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )

        // Intent to launch course screen from widget
        val courseWidgetIntent = Intent(context, CourseScreenActivity::class.java)
        courseWidgetIntent.putExtra("prefixName", "COMP")
        courseWidgetIntent.putExtra("prefixType", "UG")
        courseWidgetIntent.putExtra("code", "2011")
        courseWidgetIntent.putExtra("semesterCode", "2430")

        Column(
            modifier = GlanceModifier
                .background(GlanceTheme.colors.widgetBackground)
                .padding(16.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.Start
        ) {
            LazyColumn {
                // Course code
                item(itemId = 1) {
                    Text(
                        text = "LANG 2030H",
                        style = if (size.width >= FOUR_TWO.width) titleLarge else titleMedium,
                        modifier = GlanceModifier
                            .fillMaxWidth()
                            .clickable(actionStartActivity(courseWidgetIntent))
                    )
                }

                // Course title
                if (size.width >= FOUR_TWO.width || size.height >= TWO_THREE.height) {
                    item(itemId = 2) {
                        Text(
                            text = "Professional Development in Ethics, Innovation and Technology for Research Postgraduate Students",
                            style = bodyMedium,
                            maxLines = 1,
                            modifier = GlanceModifier
                                .fillMaxWidth()
                                .clickable(actionStartActivity(courseWidgetIntent))
                        )
                    }
                }

                item(itemId = 3) {
                    Spacer(modifier = GlanceModifier.height(8.dp))
                }

                item(itemId = 4) {
                    CompositionLocalProvider(
                        LocalMinimumInteractiveComponentSize provides Dp.Unspecified,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Display of selected section
                            Text(
                                text = "${sectionToDisplay.sectionCode} (${sectionToDisplay.classNbr})",
//                                text = "L2 (2043)",
                                style = bodyLarge.copy(color = GlanceTheme.colors.primary)
                            )

                            // Section flip and refresh buttons (Width >= 4)
                            if (size.width >= FOUR_TWO.width) {
                                Row(
                                    horizontalAlignment = Alignment.End,
                                    modifier = GlanceModifier.fillMaxWidth()
                                ) {
                                    // Prev
                                    CircleIconButton(
                                        imageProvider = ImageProvider(R.drawable.material_icon_prev),
                                        contentDescription = context.getString(R.string.prev_icon_desc),
                                        onClick = { },
                                        modifier = GlanceModifier.size(24.dp),
                                        backgroundColor = null
                                    )
                                    Spacer(modifier = GlanceModifier.width(8.dp))
                                    // Next
                                    CircleIconButton(
                                        imageProvider = ImageProvider(R.drawable.material_icon_next),
                                        contentDescription = context.getString(R.string.next_icon_desc),
                                        onClick = { },
                                        modifier = GlanceModifier.size(24.dp),
                                        backgroundColor = null
                                    )
                                    Spacer(modifier = GlanceModifier.width(8.dp))
                                    // Refresh
                                    CircleIconButton(
                                        imageProvider = ImageProvider(R.drawable.material_icon_refresh),
                                        contentDescription = context.getString(R.string.refresh_icon_desc),
                                        onClick = { },
                                        modifier = GlanceModifier.size(24.dp),
                                        backgroundColor = null
                                    )
                                }
                            }
                        }
                    }
                }

                item(itemId = 5) {
                    Spacer(modifier = GlanceModifier.height(4.dp))
                }

                // Quota of one selected section (total)
                val quotaHeadings = listOf("Quota", "Enrol", "Avail", "Wait")
                val quotaValues = listOf(sectionToDisplay.quota, sectionToDisplay.enrol, sectionToDisplay.avail, sectionToDisplay.wait)

                // Display quotas in 1 row if wide enough, 2 rows otherwise
                if (size.width >= FOUR_TWO.width) {
                    item(itemId = 6) {
                        Row(
                            modifier = GlanceModifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.Top
                        ) {
                            quotaHeadings.forEachIndexed { index, s ->
                                Column(
                                    modifier = GlanceModifier.defaultWeight(),
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(
                                        text = s,
                                        style = bodyLarge.copy(color = GlanceTheme.colors.onSurfaceVariant)
                                    )
                                    Text(
                                        text = quotaValues[index].toString(),
                                        style = headlineMedium.copy(
                                            color = GlanceTheme.colors.primary,
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                }
                            }
                        }
                    }
                } else {
                    item(itemId = 7) {
                    Column(
                        modifier = GlanceModifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        for (i in 0..1) {
                            Row(
                                modifier = GlanceModifier
                                    .fillMaxWidth()
                            ) {
                                for (j in 0..1) {
                                    Column(
                                        modifier = GlanceModifier.defaultWeight(),
                                        horizontalAlignment = Alignment.Start
                                    ) {
                                        Text(
                                            text = quotaHeadings[2 * i + j],
                                            style = bodyLarge.copy(color = GlanceTheme.colors.onSurfaceVariant)
                                        )
                                        Text(
                                            text = quotaValues[2 * i + j].toString(),
//                                            style = if (size.width >= THREE_TWO.width) {
//                                                headlineMedium.copy(
//                                                    color = GlanceTheme.colors.primary,
//                                                    fontWeight = FontWeight.Medium
//                                                )
//                                            } else {
//                                                headlineSmall.copy(
//                                                    color = GlanceTheme.colors.primary,
//                                                    fontWeight = FontWeight.Medium
//                                                )
//                                            }
                                            style = headlineSmall.copy(
                                                color = GlanceTheme.colors.primary,
                                                fontWeight = FontWeight.Medium
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                        }
                }
            }

            CompositionLocalProvider(  // Remove hardcoded padding around button
                LocalMinimumInteractiveComponentSize provides Dp.Unspecified,
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = GlanceModifier.fillMaxSize()
                ) {
                    // Display sections label if widget width >= 4
                    val semesterYear = if (size.width >= FOUR_TWO.width) "2024-25 " else ""

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        // Semester
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            if (size.width >= THREE_TWO.width) {
                                Image(
                                    provider = ImageProvider(R.drawable.material_icon_semester),
                                    contentDescription = context.getString(R.string.semester_icon_desc),
                                    modifier = GlanceModifier.size(16.dp),
                                    colorFilter = tint(GlanceTheme.colors.onSurfaceVariant)
                                )
                                Spacer(modifier = GlanceModifier.width(4.dp))
                            }
                            Text(
                                text = "${semesterYear}Summer",
                                style = labelMedium.copy(color = GlanceTheme.colors.onSurfaceVariant)
                            )
                        }

                        if (size.width >= FOUR_TWO.width) {
                            Spacer(modifier = GlanceModifier.width(8.dp))

                            // Number of units
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Image(
                                    provider = ImageProvider(R.drawable.material_icon_units),
                                    contentDescription = context.getString(R.string.units_icon_desc),
                                    modifier = GlanceModifier.size(16.dp),
                                    colorFilter = tint(GlanceTheme.colors.onSurfaceVariant)
                                )
                                Spacer(modifier = GlanceModifier.width(4.dp))
                                Text(
                                    text = "3 units",
                                    style = labelMedium.copy(color = GlanceTheme.colors.onSurfaceVariant)
                                )
                            }
                        }

                        // Section flip and refresh buttons (Width 2)
                        if (size.width < FOUR_TWO.width) {
                            Row(
                                horizontalAlignment = Alignment.End,
                                modifier = GlanceModifier.fillMaxWidth()
                            ) {
                                // Prev button
                                Image(
                                    provider = ImageProvider(R.drawable.material_icon_prev),
                                    contentDescription = context.getString(R.string.prev_icon_desc),
                                    modifier = GlanceModifier
                                        .size(16.dp)
                                        .clickable({ }),
                                    colorFilter = tint(GlanceTheme.colors.onSurfaceVariant)
                                )
                                Spacer(modifier = GlanceModifier.width(4.dp))
                                // Next button
                                Image(
                                    provider = ImageProvider(R.drawable.material_icon_next),
                                    contentDescription = context.getString(R.string.next_icon_desc),
                                    modifier = GlanceModifier
                                        .size(16.dp)
                                        .clickable({ }),
                                    colorFilter = tint(GlanceTheme.colors.onSurfaceVariant)
                                )
                                Spacer(modifier = GlanceModifier.width(4.dp))
                                // Refresh button
                                Image(
                                    provider = ImageProvider(R.drawable.material_icon_refresh),
                                    contentDescription = context.getString(R.string.refresh_icon_desc),
                                    modifier = GlanceModifier
                                        .size(16.dp)
                                        .clickable({ }),
                                    colorFilter = tint(GlanceTheme.colors.onSurfaceVariant)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}