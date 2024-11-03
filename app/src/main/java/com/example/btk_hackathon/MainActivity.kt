package com.example.btk_hackathon

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.btk_hackathon.presentation.navigation.Screen
import com.example.btk_hackathon.presentation.components.BottomBar
import com.example.btk_hackathon.presentation.navigation.MainScreenNavHost
import com.example.btk_hackathon.presentation.navigation.NavigationGraph
import com.example.btk_hackathon.ui.theme.BtkhackathonTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val languageCode = loadLanguagePreference(this)
        setLocale(this, languageCode)
        setContent {
            BtkhackathonTheme {
                val navController = rememberNavController()
                Scaffold { paddingValues ->
                    Box(
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        NavigationGraph(this@MainActivity, navController)
                    }
                }
            }
        }
    }
    override fun onRestart(){
        super.onRestart()
        val languageCode = loadLanguagePreference(this)
        setLocale(this, languageCode)
    }
}


@Composable
fun MainScreen() {
    val mainNavController = rememberNavController()
    val currentBackStackEntry by mainNavController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            if (currentBackStackEntry?.destination?.route != Screen.BookInfoScreen.route) {
                BottomBar(navController = mainNavController)
            }
            Log.d("Route", currentBackStackEntry?.destination?.route.toString())
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            MainScreenNavHost(mainNavController = mainNavController)
        }
    }
}