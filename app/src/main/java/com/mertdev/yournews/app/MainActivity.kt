package com.mertdev.yournews.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.mertdev.yournews.app.ui.theme.YourNewsTheme
import com.mertdev.yournews.helpers.AppNavGraph
import com.mertdev.yournews.helpers.bottombar.BottomBar
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YourNewsTheme {
                val navController = rememberNavController()
                Scaffold(bottomBar = { BottomBar(navController = navController) },
                    content = { paddingValues ->
                        AppNavGraph(
                            modifier = Modifier.padding(paddingValues),
                            navController = navController
                        )
                    })
            }
        }
    }
}