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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frogbubbletea.usthong.R
import com.frogbubbletea.usthong.ui.composables.CourseCard
import com.frogbubbletea.usthong.ui.theme.USThongTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseScreen(
    // TODO: Add parameter to this screen to accept a course
) {
    // Set top bar to collapse when scrolled down
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        // Connect content's scroll position to top bar so it can be collapsed
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),

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
                    // Reviews button
                    IconButton(
                        onClick = { }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.material_icon_satellite),
                            contentDescription = stringResource(id = R.string.reviews_icon_desc)
                        )
                    }

                    // Sort button
                    IconButton(
                        onClick = { }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.material_icon_open_in_browser),
                            contentDescription = stringResource(id = R.string.open_in_ust_icon_desc)
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
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Course title
            item {
                Text(
                    text = "Cultures and Values: Language, Communication and Society",
                    style = MaterialTheme.typography.titleLarge
                )
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

            item {
                CourseInfoContainer()
            }
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
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceContainerHigh
    }
    val attrTextColor = if (alert) {
        MaterialTheme.colorScheme.onPrimaryContainer
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
                fontWeight = FontWeight.Medium,
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
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Description",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "This course provides a gentle introduction to artificial intelligence (AI), and emphasizes hands-on practical experiences with Python and AI software tools to explore AI applications. Interesting applications that have been covered in previous class offerings include, but are not limited to, medical diagnosis, predictions of customer behaviour and user attitudes, character recognition, spam mail detection, text and image classifications and recognitions, sentiment analysis, and retinal vessel segmentation. The course also explores recent advances and discusses the history and ethics of AI. Only for students in their first and second year of study or those with approval from instructor by applying requisite waiver.",
                    style = MaterialTheme.typography.bodySmall
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
fun CourseScreenPreview() {
    USThongTheme {
        CourseScreen()
    }
}