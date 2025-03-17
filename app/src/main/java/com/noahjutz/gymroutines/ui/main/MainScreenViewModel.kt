package com.noahjutz.gymroutines.ui.main

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.core.view.WindowCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import com.noahjutz.gymroutines.data.AppPrefs
import com.noahjutz.gymroutines.data.ColorTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private fun Context.getActivity(): ComponentActivity? =
    when (this) {
        is ComponentActivity -> this
        is ContextWrapper -> baseContext.getActivity()
        else -> null
    }

class MainScreenViewModel(
    preferences: DataStore<Preferences>,
) : ViewModel() {
    val colorTheme =
        preferences.data.map { preferences ->
            preferences[AppPrefs.AppTheme.key]?.let { key ->
                ColorTheme.valueOf(key)
            } ?: ColorTheme.FollowSystem
        }

    val currentWorkoutId: Flow<Int> =
        preferences.data.map { preferences ->
            preferences[AppPrefs.CurrentWorkout.key] ?: -1
        }

    val showBottomLabels =
        preferences.data.map { preferences ->
            preferences[AppPrefs.ShowBottomNavLabels.key] ?: true
        }

    fun setStatusBars(
        isDark: Boolean,
        context: Context,
    ) {
        context.getActivity()?.window?.let {
            WindowCompat.getInsetsController(it, it.decorView).isAppearanceLightStatusBars = !isDark
        }
    }
}
