package com.mdrlzy.rbkweather.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mdrlzy.rbkweather.presentation.citylist.CityListScreen
import com.mdrlzy.rbkweather.presentation.home.HomeScreen
import com.mdrlzy.rbkweather.presentation.navigation.Destination
import com.mdrlzy.ui.theme.RBKWeatherTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RBKWeatherTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Destination.Home.route,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
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
