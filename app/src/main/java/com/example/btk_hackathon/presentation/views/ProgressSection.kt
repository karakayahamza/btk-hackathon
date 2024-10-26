package com.example.btk_hackathon.presentation.views

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ProgressSection(progress: Float) {
    Text(
        text = "Progress",
        style = MaterialTheme.typography.bodyMedium,
        color = Color.Gray
    )
    Spacer(modifier = Modifier.height(10.dp))

    LinearProgressIndicator(progress = progress)
    Spacer(modifier = Modifier.height(10.dp))
    Text(text = "${(progress * 100).toInt()}% Completed")
}