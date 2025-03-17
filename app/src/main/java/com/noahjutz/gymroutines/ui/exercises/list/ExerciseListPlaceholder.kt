package com.noahjutz.gymroutines.ui.exercises.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.eygraber.compose.placeholder.material3.placeholder

@Composable
fun ExerciseListPlaceholder() {
    Column {
        Box(
            Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(percent = 100))
                .placeholder(visible = true),
        )
        repeat(10) {
            ListItem(
                headlineContent = {
                    Text(
                        "A".repeat((5..15).random()),
                        modifier = Modifier.placeholder(visible = true),
                    )
                },
            )
        }
    }
}
