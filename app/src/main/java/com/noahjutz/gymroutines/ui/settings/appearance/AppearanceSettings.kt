package com.noahjutz.gymroutines.ui.settings.appearance

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.material.placeholder
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.ColorTheme
import com.noahjutz.gymroutines.ui.components.TopBar
import org.koin.androidx.compose.getViewModel

@Composable
fun AppearanceSettings(
    popBackStack: () -> Unit,
    viewModel: AppearanceSettingsViewModel = getViewModel()
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.screen_appearance_settings),
                navigationIcon = {
                    IconButton(onClick = popBackStack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.btn_pop_back))
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
        ) {
            val appTheme by viewModel.appTheme.collectAsState(initial = ColorTheme.FollowSystem)
            Row(
                Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Color Theme",
                    modifier = Modifier
                        .height(40.dp)
                        .weight(1f),
                    style = typography.displaySmall
                )
                AnimatedVisibility(
                    appTheme != ColorTheme.FollowSystem,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Spacer(Modifier.weight(1f))
                    OutlinedButton(
                        onClick = { viewModel.setAppTheme(ColorTheme.FollowSystem) },
                        modifier = Modifier.height(40.dp),
                        shape = RoundedCornerShape(percent = 100)
                    ) {
                        Text(stringResource(R.string.btn_reset))
                    }
                }
            }
            Row(Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
                ThemePreview(
                    colors = lightColorScheme(),
                    name = stringResource(ColorTheme.White.themeName),
                    selected = appTheme == ColorTheme.White || (appTheme == ColorTheme.FollowSystem && !isSystemInDarkTheme()),
                    onClick = { viewModel.setAppTheme(ColorTheme.White) }
                )
                Spacer(Modifier.width(16.dp))
                ThemePreview(
                    colors = darkColorScheme(),
                    name = stringResource(ColorTheme.Black.themeName),
                    selected = appTheme == ColorTheme.Black || (appTheme == ColorTheme.FollowSystem && isSystemInDarkTheme()),
                    onClick = { viewModel.setAppTheme(ColorTheme.Black) }
                )
            }
        }
    }
}

@Composable
private fun RowScope.ThemePreview(
    colors: ColorScheme,
    name: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        Modifier
            .weight(1f)
            .height(240.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MaterialTheme(colorScheme = colors) {
            Surface(
                modifier = Modifier
                    .weight(1f),
                onClick = onClick,
                shadowElevation = 4.dp,
                shape = RoundedCornerShape(30.dp)
            ) {
                Box(Modifier.fillMaxSize()) {
                    Column {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .background(colors.primary)
                        )
                        Text(
                            "Theme theme",
                            Modifier
                                .padding(16.dp)
                                .placeholder(visible = true)
                        )
                    }
                    Box(
                        Modifier
                            .padding(16.dp)
                            .size(60.dp)
                            .align(Alignment.BottomEnd)
                            .clip(CircleShape)
                            .background(colors.secondary)
                    ) {
                        if (selected) {
                            Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = stringResource(R.string.btn_select_option),
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(48.dp),
                                tint = colors.onSecondary
                            )
                        }
                    }
                }
            }
            Text(name, Modifier.padding(top = 16.dp))
        }
    }
}
