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

package com.noahjutz.gymroutines.ui.exercises.editor

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.ui.components.TopBar
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ExerciseEditor(
    popBackStack: () -> Unit,
    exerciseId: Int,
    viewModel: ExerciseEditorViewModel = getViewModel { parametersOf(exerciseId) }
) {
    var showDiscardAlert by remember { mutableStateOf(false) }
    if (showDiscardAlert) {
        AlertDialog(
            onDismissRequest = { showDiscardAlert = false },
            title = { Text(stringResource(R.string.dialog_title_discard)) },
            confirmButton = {
                Button(onClick = popBackStack) {
                    Text(stringResource(R.string.dialog_confirm_discard))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDiscardAlert = false }) {
                    Text(stringResource(R.string.btn_cancel))
                }
            }
        )
    }

    val isSavingEnabled by viewModel.isSavingEnabled.collectAsState(initial = false)

    BackHandler(enabled = isSavingEnabled) {
        showDiscardAlert = true
    }

    Scaffold(
        topBar = {
            TopBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (isSavingEnabled) {
                                showDiscardAlert = true
                            } else {
                                popBackStack()
                            }
                        },
                        content = {
                            Icon(
                                Icons.Default.Close,
                                stringResource(R.string.btn_cancel)
                            )
                        }
                    )
                },
                title = stringResource(R.string.screen_edit_exercise),
                actions = {
                    TextButton(
                        modifier = Modifier.padding(end = 8.dp),
                        onClick = {
                            viewModel.save {
                                popBackStack()
                            }
                        },
                        enabled = isSavingEnabled
                    ) {
                        Text(stringResource(R.string.btn_save))
                    }
                }
            )
        },
        content = { paddingValues ->
            LazyColumn(contentPadding = paddingValues) {
                item {
                    val name by viewModel.name.collectAsState()
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                        value = name,
                        onValueChange = viewModel::setName,
                        label = { Text(stringResource(R.string.label_exercise_name)) },
                        singleLine = true
                    )
                    val notes by viewModel.notes.collectAsState()
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        value = notes,
                        onValueChange = viewModel::setNotes,
                        label = { Text(stringResource(R.string.label_exercise_notes)) }
                    )
                    val logReps by viewModel.logReps.collectAsState()
                    ListItem(
                        modifier = Modifier.toggleable(
                            value = logReps,
                            onValueChange = viewModel::setLogReps
                        ),
                        headlineContent = { Text(stringResource(R.string.checkbox_log_reps)) },
                        leadingContent = { Checkbox(checked = logReps, null) }
                    )
                    val logWeight by viewModel.logWeight.collectAsState()
                    ListItem(
                        modifier = Modifier.toggleable(
                            value = logWeight,
                            onValueChange = viewModel::setLogWeight
                        ),
                        headlineContent = { Text(stringResource(R.string.checkbox_log_weight)) },
                        leadingContent = { Checkbox(checked = logWeight, null) }
                    )
                    val logTime by viewModel.logTime.collectAsState()
                    ListItem(
                        modifier = Modifier.toggleable(
                            value = logTime,
                            onValueChange = viewModel::setLogTime
                        ),
                        headlineContent = { Text(stringResource(R.string.checkbox_log_time)) },
                        leadingContent = { Checkbox(checked = logTime, null) }
                    )
                    val logDistance by viewModel.logDistance.collectAsState()
                    ListItem(
                        modifier = Modifier.toggleable(
                            value = logDistance,
                            onValueChange = viewModel::setLogDistance
                        ),
                        headlineContent = { Text(stringResource(R.string.checkbox_log_distance)) },
                        leadingContent = { Checkbox(checked = logDistance, null) }
                    )
                }
            }
        }
    )
}
