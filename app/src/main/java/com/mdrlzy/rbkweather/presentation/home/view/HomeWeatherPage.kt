package com.mdrlzy.rbkweather.presentation.home.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mdrlzy.rbkweather.presentation.home.components.Average
import com.mdrlzy.rbkweather.presentation.home.components.DailyForecastCard
import com.mdrlzy.rbkweather.presentation.home.components.FeelsLike
import com.mdrlzy.rbkweather.presentation.home.components.HomeHeader
import com.mdrlzy.rbkweather.presentation.home.components.HourlyCard
import com.mdrlzy.rbkweather.presentation.home.components.Humidity
import com.mdrlzy.rbkweather.presentation.home.components.Pressure
import com.mdrlzy.rbkweather.presentation.home.components.Sunset
import com.mdrlzy.rbkweather.presentation.home.components.UVIndex
import com.mdrlzy.rbkweather.presentation.home.components.WindCard
import com.mdrlzy.rbkweather.presentation.home.model.DailyForecastUi
import com.mdrlzy.rbkweather.presentation.home.model.HomeWeatherPageUiState
import com.mdrlzy.rbkweather.presentation.home.model.HourlyUiModel
import com.mdrlzy.ui.theme.CoreRDrawable
import com.mdrlzy.ui.theme.RBKWeatherTheme
import java.time.OffsetDateTime

@Composable
fun HomeWeatherPage(
    pageState: HomeWeatherPageUiState,
    isCelciusNotFarenheit: Boolean,
    modifier: Modifier = Modifier,
) {
    val statusBarTopPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val navigationBarBottomPadding = WindowInsets.navigationBars
        .asPaddingValues()
        .calculateBottomPadding()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(
            start = 18.dp,
            end = 18.dp,
            top = 72.dp + statusBarTopPadding,
            bottom = 120.dp + navigationBarBottomPadding,
        )
    ) {
        item {
            HomeHeader(
                state = pageState,
                isCelciusNotFarenheit = isCelciusNotFarenheit,
            )
            Spacer(Modifier.height(68.dp))
        }

        item {
            HourlyCard(
                description = pageState.detailedDescription,
                hourItems = pageState.hourlyItems,
                isCelciusNotFarenheit = isCelciusNotFarenheit,
            )
        }

        item {
            DailyForecastCard(
                dailyItems = pageState.dailyItems,
                isCelciusNotFarenheit = isCelciusNotFarenheit,
            )
        }

        item {
            Row {
                Average(
                    modifier = Modifier.weight(1f),
                    averageTemp = pageState.temp,
                    isCelciusNotFarenheit = isCelciusNotFarenheit,
                )
                Spacer(Modifier.width(8.dp))
                FeelsLike(
                    modifier = Modifier.weight(1f),
                    feelsLike = pageState.feelsLike,
                    isCelciusNotFarenheit = isCelciusNotFarenheit,
                )
            }
        }

        item {
            WindCard(
                windSpeed = pageState.windSpeed,
                windMaxSpeed = pageState.windMaxSpeed,
                windDirectionDegrees = pageState.windDirectionDegrees,
            )
        }

        item {
            Row {
                Humidity(
                    modifier = Modifier.weight(1f),
                    humidity = pageState.humidity,
                    isCelciusNotFarenheit = isCelciusNotFarenheit,
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

@Preview(showBackground = true, backgroundColor = 0xFF2B4F73)
@Composable
private fun HomeWeatherPagePreview() {
    RBKWeatherTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(CoreRDrawable.bg_clear_day),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
            )
            HomeWeatherPage(
                pageState = HomeWeatherPageUiState(
                    city = "Astana",
                    description = "Clear",
                    detailedDescription = "Clear conditions for the hour. Wind is light.",
                    hourlyItems = listOf(
                        HourlyUiModel(hour = "18", temperature = 18, isCurrent = true),
                        HourlyUiModel(hour = "19", temperature = 17),
                        HourlyUiModel(hour = "20", temperature = 16),
                        HourlyUiModel(hour = "21", temperature = 15),
                        HourlyUiModel(hour = "22", temperature = 14),
                    ),
                    dailyItems = listOf(
                        DailyForecastUi(day = "Mon", minTemp = 12, maxTemp = 21, isToday = true),
                        DailyForecastUi(day = "Tue", minTemp = 10, maxTemp = 18),
                        DailyForecastUi(day = "Wed", minTemp = 8, maxTemp = 17),
                        DailyForecastUi(day = "Thu", minTemp = 9, maxTemp = 19),
                        DailyForecastUi(day = "Fri", minTemp = 11, maxTemp = 20),
                    ),
                    temp = 18,
                    minTemp = 12,
                    maxTemp = 21,
                    feelsLike = 17,
                    humidity = 64,
                    pressure = 1018,
                    uvIndex = 3,
                    windSpeed = 4,
                    windDirectionDegrees = 45,
                    windMaxSpeed = 8,
                    sunsetTime = OffsetDateTime.parse("2026-05-10T20:24:00+03:00"),
                    sunriseTime = OffsetDateTime.parse("2026-05-10T04:12:00+03:00"),
                ),
                isCelciusNotFarenheit = true,
            )
        }
    }
}
