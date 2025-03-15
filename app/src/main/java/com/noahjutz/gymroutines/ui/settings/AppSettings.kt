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

package com.noahjutz.gymroutines.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.ui.components.TopBar

@Composable
fun AppSettings(
    popBackStack: () -> Unit,
    navToAbout: () -> Unit,
    navToAppearanceSettings: () -> Unit,
    navToDataSettings: () -> Unit,
    navToGeneralSettings: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.screen_settings),
                navigationIcon = {
                    IconButton(onClick = popBackStack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.btn_pop_back))
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            Modifier.scrollable(
                orientation = Orientation.Vertical,
                state = rememberScrollState()
            ).padding(paddingValues)
        ) {
            ListItem(
                modifier = Modifier.clickable(onClick = navToGeneralSettings),
                headlineContent = { Text(stringResource(R.string.screen_general_settings)) },
                leadingContent = { Icon(Icons.Default.Construction, null) }
            )
            ListItem(
                modifier = Modifier.clickable(onClick = navToAppearanceSettings),
                headlineContent = { Text(stringResource(R.string.screen_appearance_settings)) },
                leadingContent = { Icon(Icons.Default.DarkMode, null) }
            )
            ListItem(
                modifier = Modifier.clickable(onClick = navToDataSettings),
                headlineContent = { Text(stringResource(R.string.screen_data_settings)) },
                leadingContent = { Icon(Icons.Default.Shield, null) }
            )
            HorizontalDivider()
            ListItem(
                modifier = Modifier.clickable(onClick = navToAbout),
                headlineContent = { Text(stringResource(R.string.screen_about)) },
                leadingContent = { Icon(Icons.Default.Info, null) }
            )
        }
    }
}
