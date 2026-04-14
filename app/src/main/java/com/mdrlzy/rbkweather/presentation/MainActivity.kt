package com.mdrlzy.rbkweather.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.mdrlzy.rbkweather.data.RetrofitClient
import com.mdrlzy.rbkweather.data.repo.WeatherRepoImpl
import com.mdrlzy.ui.theme.RBKWeatherTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        lifecycleScope.launch {
            testRequest()
        }
        setContent {
            RBKWeatherTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RBKWeatherTheme {
        Greeting("Android")
    }
}

suspend fun testRequest() {
    val weatherResult = WeatherRepoImpl(RetrofitClient.api)
        .getCurrent(
            lat = 43.2389,
            lon = 76.8897,
        )

    weatherResult.fold(
        onSuccess = {
            Log.d("TestRequest", it.toString())
        },
        onFailure = {
            Log.d("TestRequest", it.message.toString())
            it.printStackTrace()
        }
    )
}