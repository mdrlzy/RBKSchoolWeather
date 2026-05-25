package com.mdrlzy.rbkweather.presentation

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mdrlzy.rbkweather.presentation.citylist.CityListScreen
import com.mdrlzy.rbkweather.presentation.home.view.HomeScreen
import com.mdrlzy.rbkweather.presentation.navigation.Destination
import com.mdrlzy.ui.theme.RBKWeatherTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
        setContent {
            RBKWeatherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Destination.Home.route,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        composable(Destination.Home.route) {
                            HomeScreen(
                                onNavigateToCityList = {
                                    navController.navigate(Destination.CityList.route)
                                },
                            )
                        }
                        composable(Destination.CityList.route) {
                            CityListScreen()
                        }
                    }
                }
            }
        }
    }
}
