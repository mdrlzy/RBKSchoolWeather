package com.mdrlzy.rbkweather.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeWeatherPage(
    pageState: HomeWeatherPageUiState,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(
            start = 18.dp,
            end = 18.dp,
            top = 72.dp,
            bottom = 40.dp
        )
    ) {
        item {
            HomeHeader(pageState)
            Spacer(Modifier.height(68.dp))
        }

        item {
            HourlyCard(
                pageState.detailedDescription,
                pageState.hourlyItems
            )
        }

        item {
            DailyForecastCard(pageState.dailyItems)
        }

        item {
            Row {
                Average(
                    modifier = Modifier.weight(1f),
                    averageTemp = pageState.temp,
                )
                Spacer(Modifier.width(8.dp))
                FeelsLike(
                    modifier = Modifier.weight(1f),
                    feelsLike = pageState.feelsLike,
                )
            }
        }

        item {
            WindCard(
                windSpeed = pageState.windSpeed,
                windMaxSpeed = pageState.windMaxSpeed,
                windDirection = pageState.windDirection,
            )
        }

        item {
            Row {
                Humidity(
                    modifier = Modifier.weight(1f),
                    humidity = pageState.humidity,
                )
                Spacer(Modifier.width(8.dp))
                Pressure(
                    modifier = Modifier.weight(1f),
                    pressure = pageState.pressure,
                )
            }
        }

        item {
            Row {
                UVIndex(
                    modifier = Modifier.weight(1f),
                    uvIndex = pageState.uvIndex,
                )
                Spacer(Modifier.width(8.dp))
                Sunset(
                    modifier = Modifier.weight(1f),
                    sunsetTime = pageState.sunsetTime,
                    sunriseTime = pageState.sunriseTime,
                )
            }
        }
    }
}
