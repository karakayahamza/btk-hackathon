package com.example.btk_hackathon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.btk_hackathon.ui.navigation.BottomBar
import com.example.btk_hackathon.ui.navigation.NavigationGraph
import com.example.btk_hackathon.ui.theme.BtkhackathonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BtkhackathonTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                }
            }
        }
    }
}


@Composable
fun MainScreen() {
    val navController: NavHostController = rememberNavController()
    var buttonsVisible by remember { mutableStateOf(true) }
    Scaffold(
        bottomBar = {
            if (buttonsVisible) {
                BottomBar(
                    navController = navController,
                    modifier = Modifier
                )
            }
        }) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            NavigationGraph(navController = navController) { isVisible ->
                buttonsVisible = isVisible
            }
        }
    }
}


@Preview
@Composable
fun Preview(modifier: Modifier = Modifier) {

    MainScreen()

}