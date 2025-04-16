package com.frogbubbletea.ustcoursemobile.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.frogbubbletea.ustcoursemobile.data.Prefix
import com.frogbubbletea.ustcoursemobile.data.Semester

// Bottom sheet navigation menu
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreMenu(
    semesters: List<Semester>,
    prefixes: List<Prefix>,
    selectedSem: Semester,
    selectedPrefix: Prefix,
    onSelectStarred: () -> Unit = { },
    onSelectSem: (Semester) -> Unit = { },
    onSelectPrefix: (Prefix) -> Unit = { },
    onDismissRequest: () -> Unit = { },
    sheetState: SheetState,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = Modifier.statusBarsPadding()
    ) {
        LazyColumn {
            // Starred
            // TODO: Implement course starring function
//            item {
//                Surface(
//                    modifier = Modifier.fillMaxWidth(),
//                    color = Color.Transparent,
//                    onClick = {
//                        // TODO: Redirect to starred screen
//                        onSelectStarred()
//                    }
//                ) {
//                    Row(
//                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.spacedBy(8.dp)
//                    ) {
//                        Icon(
//                            painter = painterResource(R.drawable.material_icon_star),
//                            contentDescription = stringResource(id = R.string.star_icon_desc),
//                            modifier = Modifier.size(20.dp),
//                            tint = MaterialTheme.colorScheme.onSurface
//                        )
//                        Text("Starred courses")
//                    }
//                }
//            }
//
//            item {
//                HorizontalDivider(
//                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
//                )
//            }

            // Select semester
            item {
                Text(
                    text = "Semesters",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
            items(semesters) { semester ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = if (semester == selectedSem) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        Color.Transparent
                    },
                    onClick = { onSelectSem(semester) }
                ) {
                    Text(
                        text = semester.name,
                        color = if (semester == selectedSem) {
                            MaterialTheme.colorScheme.onSecondaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }
            }

            item {
                HorizontalDivider(
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )
            }

            // Select prefix
            item {
                Text(
                    text = "Prefixes",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
            items(prefixes) { prefix ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = if (prefix == selectedPrefix) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        Color.Transparent
                    },
                    onClick = { onSelectPrefix(prefix) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = prefix.name,
                            color = if (prefix == selectedPrefix) {
                                MaterialTheme.colorScheme.onSecondaryContainer
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
                        )
//                        Text(
//                            text = "Information Systems, Business Statistics and Operations Management",
//                            color = MaterialTheme.colorScheme.onSurfaceVariant,
//                            style = MaterialTheme.typography.bodySmall
//                        )
                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        ) {
                            Box(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = prefix.type.toString(),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}