package com.frogbubbletea.usthong.ui.screens

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frogbubbletea.usthong.R
import com.frogbubbletea.usthong.ui.composables.CourseCard
import com.frogbubbletea.usthong.ui.composables.SectionCard
import com.frogbubbletea.usthong.ui.theme.USThongTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseScreen(
    // TODO: Add parameter to this screen to accept a course

    onNavigateBack: () -> Unit = {},
) {
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
                    Text("LANG 2030H")
                },

                // Back button
                navigationIcon = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
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
                        text = "Cultures and Values: Language, Communication and Society",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            // Course attributes
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CourseAttribute("Matching (LA)", true)
                    CourseAttribute("4Y")
                    CourseAttribute("CC22")
                    CourseAttribute("MEDI")
                    CourseAttribute("READ")
                    CourseAttribute("DELI")
                }
            }

            // Course info
            item {
                CourseInfoContainer()
            }

            // Sections
            items(40) {
                SectionCard()
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
    attr: String,
    alert: Boolean = false
) {
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
        color = attrBgColor
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = attr,
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
    // TODO: Add parameter to make it accept a map of course info titles and content
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
                Column {
                    Text(
                        text = "Pre-requisite",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "(Grade B+ or above in COMP 2011 / COMP 2012 / COMP 2012H) AND (grade A- or above in COMP 2711 / COMP 2711H / MATH 2343)",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }

                Column {
                    Text(
                        text = "Exclusion",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "COMP 3211, COMP 4211, COMP 4221, COMP 4331, COMP 4332, COMP 4421, COMP 4471, COMP 4901K, COMP 4901L, ELEC 4130, ELEC 4230, EMIA 4110, ISOM 3360, MATH 4336, MATH 4432, RMBI 4310, COMP 5211, COMP 5212, COMP 5213, COMP 5221, COMP 5222, COMP 5215, COMP 5331, COMP 5421",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }

                Column {
                    Text(
                        text = "Description",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "This course provides a gentle introduction to artificial intelligence (AI), and emphasizes hands-on practical experiences with Python and AI software tools to explore AI applications. Interesting applications that have been covered in previous class offerings include, but are not limited to, medical diagnosis, predictions of customer behaviour and user attitudes, character recognition, spam mail detection, text and image classifications and recognitions, sentiment analysis, and retinal vessel segmentation. The course also explores recent advances and discusses the history and ethics of AI. Only for students in their first and second year of study or those with approval from instructor by applying requisite waiver.",
                        style = MaterialTheme.typography.bodySmall,
                    )
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
fun CourseScreenPreview() {
    USThongTheme {
        CourseScreen()
    }
}