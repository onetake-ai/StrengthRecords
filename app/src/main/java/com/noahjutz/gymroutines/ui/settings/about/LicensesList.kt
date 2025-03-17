package com.noahjutz.gymroutines.ui.settings.about

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.ui.components.TopBar
import com.noahjutz.gymroutines.util.openUrl

enum class Licenses(val fullName: String) {
    APACHE2("Apache License 2.0"),
    EPL1("Eclipse Public License 1.0"),
}

data class Dependency(
    val name: String,
    val license: Licenses,
    val url: String,
)

val dependencies =
    listOf(
        Dependency(
            name = "Koin",
            license = Licenses.APACHE2,
            url = "https://github.com/InsertKoinIO/koin",
        ),
        Dependency(
            name = "Android Jetpack",
            license = Licenses.APACHE2,
            url = "https://developer.android.com/jetpack",
        ),
        Dependency(
            name = "Kotlinx Coroutines",
            license = Licenses.APACHE2,
            url = "https://github.com/Kotlin/kotlinx.coroutines",
        ),
        Dependency(
            name = "Kotlinx Serialization",
            license = Licenses.APACHE2,
            url = "https://github.com/Kotlin/kotlinx.serialization",
        ),
        Dependency(
            name = "ProcessPhoenix",
            license = Licenses.APACHE2,
            url = "https://github.com/JakeWharton/ProcessPhoenix",
        ),
        Dependency(
            name = "Junit 4",
            license = Licenses.EPL1,
            url = "https://github.com/junit-team/junit4",
        ),
        Dependency(
            name = "AssertJ",
            license = Licenses.APACHE2,
            url = "https://github.com/assertj/assertj-core",
        ),
        Dependency(
            name = "MockK",
            license = Licenses.APACHE2,
            url = "https://github.com/mockk/mockk",
        ),
        Dependency(
            name = "Accompanist",
            license = Licenses.APACHE2,
            url = "https://github.com/google/accompanist",
        ),
    )

@Composable
fun LicensesList(popBackStack: () -> Unit) {
    Scaffold(
        topBar = {
            TopBar(
                navigationIcon = {
                    IconButton(onClick = popBackStack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            stringResource(R.string.btn_pop_back),
                        )
                    }
                },
                title = stringResource(R.string.screen_licenses),
            )
        },
    ) { paddingValues ->
        LazyColumn(contentPadding = paddingValues) {
            item {
                Card(
                    Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
                    shape = RoundedCornerShape(30.dp),
                ) {
                    Box(Modifier.padding(20.dp)) {
                        Text(stringResource(R.string.app_license), style = typography.bodySmall)
                    }
                }
                Text(
                    stringResource(R.string.title_dependencies_list),
                    style = typography.displaySmall,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
                )
                Spacer(Modifier.height(16.dp))
            }

            items(dependencies) { dependency ->
                val context = LocalContext.current
                ListItem(
                    modifier = Modifier.clickable { context.openUrl(dependency.url) },
                    headlineContent = { Text(dependency.name) },
                    supportingContent = { Text(dependency.license.fullName) },
                    trailingContent = {
                        Icon(
                            Icons.Default.OpenInBrowser,
                            stringResource(R.string.btn_open_in_browser),
                        )
                    },
                )
            }
        }
    }
}
