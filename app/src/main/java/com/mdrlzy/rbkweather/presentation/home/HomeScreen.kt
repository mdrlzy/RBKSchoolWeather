@file:OptIn(ExperimentalMaterial3Api::class)

package com.mdrlzy.rbkweather.presentation.home

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mdrlzy.ui.theme.CoreRDrawable
import com.mdrlzy.ui.theme.CoreRString
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val pullRefreshState = rememberPullToRefreshState()
    val weatherLoadFailedMessage = stringResource(CoreRString.home_toast_weather_load_failed)
    val locationPermissionDeniedMessage =
        stringResource(CoreRString.home_toast_location_permission_denied)

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.onLocationPermissionResult(granted)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeEffect.RequestLocationPermission -> {
                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }

                is HomeEffect.WeatherLoadFailed -> {
                    Toast.makeText(
                        context,
                        weatherLoadFailedMessage,
                        Toast.LENGTH_SHORT,
                    ).show()
                }

                is HomeEffect.LocationPermissionDenied -> {
                    Toast.makeText(
                        context,
                        locationPermissionDeniedMessage,
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        }
    }

    if (!state.isInitialized) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullToRefresh(
                state = pullRefreshState,
                isRefreshing = state.isRefreshing,
                onRefresh = { viewModel.onRefresh() }
            )
    ) {

        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(CoreRDrawable.bg_clear_day),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(
                start = 18.dp,
                end = 18.dp,
                top = 72.dp,
                bottom = 40.dp
            )
        ) {

            item {
                HomeHeader(state)
                Spacer(Modifier.height(68.dp))
            }

            item {
                HourlyCard(
                    state.detailedDescription,
                    state.hourlyItems
                )
            }

            item {
                DailyForecastCard(state.dailyItems)
            }

            item {
                Row {
                    Average(
                        modifier = Modifier.weight(1f),
                        averageTemp = state.temp,
                    )
                    Spacer(Modifier.width(8.dp))
                    FeelsLike(
                        modifier = Modifier.weight(1f),
                        feelsLike = state.feelsLike,
                    )
                }
            }

            item {
                WindCard(
                    windSpeed = state.windSpeed,
                    windMaxSpeed = state.windMaxSpeed,
                    windDirection = state.windDirection,
                )
            }

            item {
                Row {
                    Humidity(
                        modifier = Modifier.weight(1f),
                        humidity = state.humidity,
                    )
                    Spacer(Modifier.width(8.dp))
                    Pressure(
                        modifier = Modifier.weight(1f),
                        pressure = state.pressure,
                    )
                }
            }

            item {
                Row {
                    UVIndex(
                        modifier = Modifier.weight(1f),
                        uvIndex = state.uvIndex,
                    )
                    Spacer(Modifier.width(8.dp))
                    Sunset(
                        modifier = Modifier.weight(1f),
                        sunsetTime = state.sunsetTime,
                        sunriseTime = state.sunriseTime,
                    )
                }
            }
        }
    }
}
