/*
 * Splitfit
 * Copyright (C) 2020  Noah Jutz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.noahjutz.gymroutines.ui.exercises.list

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.domain.Exercise
import com.noahjutz.gymroutines.ui.components.SearchBar
import com.noahjutz.gymroutines.ui.components.SwipeToDeleteBackground
import com.noahjutz.gymroutines.ui.components.TopBar
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExerciseList(
    navToExerciseEditor: (Int) -> Unit,
    navToSettings: () -> Unit,
    viewModel: ExerciseListViewModel = getViewModel(),
) {
    Scaffold(
        contentWindowInsets = WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal),
        topBar = {
            TopBar(
                title = stringResource(R.string.screen_exercise_list),
                actions = {
                    Box {
                        var expanded by remember { mutableStateOf(false) }
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(Icons.Default.MoreVert, stringResource(R.string.btn_more))
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                        ) {
                            DropdownMenuItem(
                                onClick = navToSettings,
                                text = { Text("Settings") },
                            )
                        }
                    }
                },
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navToExerciseEditor(-1) },
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text(stringResource(R.string.btn_new_exercise)) },
            )
        },
    ) { paddingValues ->
        val exercises by viewModel.exercises.collectAsState(null)

        Crossfade(exercises != null, Modifier.padding(paddingValues)) { isReady ->
            if (isReady) {
                ExerciseListContent(
                    navToExerciseEditor = navToExerciseEditor,
                    exercises = exercises ?: emptyList(),
                    viewModel = viewModel,
                )
            } else {
                ExerciseListPlaceholder()
            }
        }
    }
}

@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalFoundationApi::class,
)
@Composable
private fun ExerciseListContent(
    exercises: List<Exercise>,
    navToExerciseEditor: (Int) -> Unit,
    viewModel: ExerciseListViewModel,
) {
    val scope = rememberCoroutineScope()
    LazyColumn(Modifier.fillMaxHeight()) {
        item {
            val searchQuery by viewModel.nameFilter.collectAsState()
            SearchBar(
                modifier =
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                value = searchQuery,
                onValueChange = viewModel::setNameFilter,
            )
        }

        items(exercises.filter { !it.hidden }, { it.exerciseId }) { exercise ->
            val dismissState = rememberSwipeToDismissBoxState()

            SwipeToDismissBox(
                modifier =
                    Modifier
                        .animateItemPlacement(),
                // .zIndex(if (dismissState.offset.value == 0f) 0f else 1f),
                state = dismissState,
                backgroundContent = { SwipeToDeleteBackground(dismissState) },
            ) {
                Card(
                    elevation =
                        CardDefaults.cardElevation(
                            defaultElevation = 0.dp,
                            draggedElevation = 6.dp, // TODO elevation stays at 0
                        ),
                ) {
                    ListItem(
                        modifier = Modifier.clickable { navToExerciseEditor(exercise.exerciseId) },
                        headlineContent = {
                            Text(
                                text = exercise.name,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        },
                        trailingContent = {
                            Box {
                                var expanded by remember { mutableStateOf(false) }
                                IconButton(onClick = { expanded = !expanded }) {
                                    Icon(Icons.Default.MoreVert, null)
                                }
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                ) {
                                    DropdownMenuItem(
                                        onClick = {
                                            expanded = false
                                            scope.launch {
                                                dismissState.dismiss(SwipeToDismissBoxValue.StartToEnd)
                                            }
                                        },
                                        text = {
                                            Text(stringResource(R.string.btn_delete))
                                        },
                                    )
                                }
                            }
                        },
                    )
                }
            }

            if (dismissState.targetValue != SwipeToDismissBoxValue.Settled) {
                ConfirmDeleteExerciseDialog(
                    onDismiss = { scope.launch { dismissState.reset() } },
                    exerciseName = exercise.name,
                    onConfirm = { viewModel.delete(exercise) },
                )
            }
        }
        item {
            // Fix FAB overlap
            Spacer(Modifier.height(72.dp))
        }
    }
}

@Composable
private fun ConfirmDeleteExerciseDialog(
    exerciseName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        title = {
            Text(
                stringResource(R.string.dialog_title_delete, exerciseName),
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                content = { Text(stringResource(R.string.btn_delete)) },
            )
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                content = { Text(stringResource(R.string.btn_cancel)) },
            )
        },
        onDismissRequest = onDismiss,
    )
}
