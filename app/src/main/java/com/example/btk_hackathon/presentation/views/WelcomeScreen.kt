package com.example.btk_hackathon.presentation.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun WelcomeScreen(onGetStartedClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFEFEF)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Welcome to OgrenGenc",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "“Education is the most powerful weapon you can use to change the world.”",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = onGetStartedClick,
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Get Started")
            }
        }
    }
}
