package com.mdrlzy.rbkweather.presentation.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mdrlzy.rbkweather.presentation.home.model.DailyForecastUi
import com.mdrlzy.rbkweather.presentation.home.model.HomeWeatherPageUiState
import com.mdrlzy.rbkweather.presentation.home.model.HourlyUiModel
import com.mdrlzy.ui.theme.RBKWeatherTheme
import java.time.OffsetDateTime

@Preview(showBackground = true, backgroundColor = 0xFF2B4F73)
@Composable
private fun HomeHeaderPreview() {
    RBKWeatherTheme {
        HomeHeader(state = previewHomeWeatherPageState())
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2B4F73)
@Composable
private fun HomeBottomNavBarPreview() {
    RBKWeatherTheme {
        HomeBottomNavBar(
            currentPage = 1,
            pageCount = 3,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2B4F73)
@Composable
private fun HourlyCardPreview() {
    RBKWeatherTheme {
        HourlyCard(
            description = "Clear conditions for the hour. Wind is light.",
            hourItems = previewHourlyItems(),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2B4F73)
@Composable
private fun DailyForecastCardPreview() {
    RBKWeatherTheme {
        DailyForecastCard(dailyItems = previewDailyItems())
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2B4F73)
@Composable
private fun AveragePreview() {
    RBKWeatherTheme {
        Average(
            modifier = Modifier.width(180.dp),
            averageTemp = 18,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2B4F73)
@Composable
private fun FeelsLikePreview() {
    RBKWeatherTheme {
        FeelsLike(
            modifier = Modifier.width(180.dp),
            feelsLike = 17,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2B4F73)
@Composable
private fun HumidityPreview() {
    RBKWeatherTheme {
        Humidity(
            modifier = Modifier.width(180.dp),
            humidity = 64,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2B4F73)
@Composable
private fun PressurePreview() {
    RBKWeatherTheme {
        Pressure(
            modifier = Modifier.width(180.dp),
            pressure = 1018,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2B4F73)
@Composable
private fun UVIndexPreview() {
    RBKWeatherTheme {
        UVIndex(
            modifier = Modifier.width(180.dp),
            uvIndex = 3,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2B4F73)
@Composable
private fun SunsetPreview() {
    RBKWeatherTheme {
        Sunset(
            modifier = Modifier.width(180.dp),
            sunsetTime = OffsetDateTime.parse("2026-05-10T20:24:00+03:00"),
            sunriseTime = OffsetDateTime.parse("2026-05-10T04:12:00+03:00"),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2B4F73)
@Composable
private fun WindCardPreview() {
    RBKWeatherTheme {
        WindCard(
            windSpeed = 4,
            windMaxSpeed = 8,
            windDirectionDegrees = 45,
        )
    }
}

private fun previewHomeWeatherPageState() = HomeWeatherPageUiState(
    city = "Astana",
    description = "Clear",
    detailedDescription = "Clear conditions for the hour. Wind is light.",
    hourlyItems = previewHourlyItems(),
    dailyItems = previewDailyItems(),
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
)

private fun previewHourlyItems() = listOf(
    HourlyUiModel(hour = "18", temperature = 18, isCurrent = true),
    HourlyUiModel(hour = "19", temperature = 17),
    HourlyUiModel(hour = "20", temperature = 16),
    HourlyUiModel(hour = "21", temperature = 15),
    HourlyUiModel(hour = "22", temperature = 14),
)

private fun previewDailyItems() = listOf(
    DailyForecastUi(day = "Mon", minTemp = 12, maxTemp = 21, isToday = true),
    DailyForecastUi(day = "Tue", minTemp = 10, maxTemp = 18),
    DailyForecastUi(day = "Wed", minTemp = 8, maxTemp = 17),
    DailyForecastUi(day = "Thu", minTemp = 9, maxTemp = 19),
    DailyForecastUi(day = "Fri", minTemp = 11, maxTemp = 20),
)
