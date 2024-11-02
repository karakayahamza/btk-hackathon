package com.example.btk_hackathon.presentation.screens.splash_screen

import android.content.Context
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.btk_hackathon.MainActivity
import com.example.btk_hackathon.R
import com.example.btk_hackathon.presentation.navigation.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(navController: NavHostController, context: MainActivity) {
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.8f) }

    LaunchedEffect(key1 = true) {
        launch {
            alpha.animateTo(1f, animationSpec = tween(1000))
        }
        launch {
            scale.animateTo(1f, animationSpec = tween(1000, easing = FastOutSlowInEasing))
        }

        delay(1000)

        if (onBoardingIsFinished(context = context)) {
            navController.popBackStack()
            navController.navigate(Screen.MainScreen.route) {
                popUpTo(Screen.SplashScreen.route) { inclusive = true }
            }
        } else {
            navController.popBackStack()
            navController.navigate(Screen.OnBoardScreen.route) {
                popUpTo(Screen.SplashScreen.route) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(R.drawable.book_app_icon),
            contentDescription = "App Icon",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(200.dp)
                .scale(scale.value)
        )

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = "BookNest",
            modifier = Modifier.alpha(alpha.value),
            fontSize = 52.sp,
            fontWeight = FontWeight.Light,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

private fun onBoardingIsFinished(context: MainActivity): Boolean {
    val sharedPreferences = context.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("isFinished", false)
}