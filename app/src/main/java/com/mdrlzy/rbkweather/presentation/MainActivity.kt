package com.mdrlzy.rbkweather.presentation

import android.Manifest
import android.content.pm.PackageManager
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationServices
import com.mdrlzy.rbkweather.data.RetrofitClient
import com.mdrlzy.rbkweather.data.repo.LocationRepoImpl
import com.mdrlzy.rbkweather.data.repo.WeatherRepoImpl
import com.mdrlzy.rbkweather.domain.usecase.GetCurrentWeatherUseCase
import com.mdrlzy.ui.theme.RBKWeatherTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        lifecycleScope.launch {
            requestPermissionOrMakeRequest()
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

    suspend fun requestPermissionOrMakeRequest() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            makeRequest()
        } else {
            requestPermission()
        }
    }

    fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            100
        )
    }

    suspend fun makeRequest() {
        val locationClient = LocationServices.getFusedLocationProviderClient(this)
        val locationRepo = LocationRepoImpl(locationClient)
        val weatherRepo = WeatherRepoImpl(RetrofitClient.api)

        val getCurrentWeatherUseCase = GetCurrentWeatherUseCase(weatherRepo, locationRepo)

        val weatherResult = getCurrentWeatherUseCase()

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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 100 && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            lifecycleScope.launch {
                makeRequest()
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