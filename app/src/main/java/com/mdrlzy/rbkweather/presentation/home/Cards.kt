package com.mdrlzy.rbkweather.presentation.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mdrlzy.ui.components.AppHorDivider
import com.mdrlzy.ui.components.InfoCardBasic
import com.mdrlzy.ui.components.InfoCardSmall
import com.mdrlzy.ui.theme.CoreRDrawable
import com.mdrlzy.ui.theme.CoreRString
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Composable
fun Average(
    modifier: Modifier,
    averageTemp: Int,
) {
    InfoCardSmall(
        modifier = modifier,
        icon = CoreRDrawable.graph,
        title = stringResource(CoreRString.in_average),
    ) {
        Text(
            text = "На ${averageTemp}°",
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}

@Composable
fun FeelsLike(
    modifier: Modifier,
    feelsLike: Int,
) {
    InfoCardSmall(
        modifier = modifier,
        icon = CoreRDrawable.graph,
        title = stringResource(CoreRString.feels_like),
    ) {
        Text(
            text = "${feelsLike}°",
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}

@Composable
fun Humidity(
    modifier: Modifier,
    humidity: Int,
) {
    InfoCardSmall(
        modifier = modifier,
        icon = CoreRDrawable.humidity,
        title = stringResource(CoreRString.humidity),
    ) {
        Text(
            text = "${humidity}%",
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(text = "Точка росы сейчас: 5°.")
    }
}

@Composable
fun Pressure(
    modifier: Modifier,
    pressure: Int,
) {
    InfoCardSmall(
        modifier = modifier,
        icon = CoreRDrawable.squeeze,
        title = stringResource(CoreRString.pressure),
    ) {
        Text(
            text = pressure.toString(),
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(text = " гПА")
    }
}

@Composable
fun UVIndex(
    modifier: Modifier,
    uvIndex: Int,
) {
    InfoCardSmall(
        modifier = modifier,
        icon = CoreRDrawable.squeeze,
        title = stringResource(CoreRString.uv_index),
    ) {
        Text(
            text = uvIndex.toString(),
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}

@Composable
fun Sunset(
    modifier: Modifier,
    sunsetTime: OffsetDateTime,
    sunriseTime: OffsetDateTime,
) {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    InfoCardSmall(
        modifier = modifier,
        icon = CoreRDrawable.sunset,
        title = stringResource(CoreRString.sunset),
    ) {
        Text(
            text = sunsetTime.format(timeFormatter),
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(text = "Восход в ${sunriseTime.format(timeFormatter)}")
    }
}

@Composable
fun WindCard(
    windSpeed: Int,
    windMaxSpeed: Int,
    windDirection: String,
) {
    InfoCardBasic(
        icon = CoreRDrawable.wind,
        title = stringResource(CoreRString.wind),
    ) {
        Row {
            Text(text = "Ветер", style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.weight(1f))
            Text(text = "$windSpeed км/ч", style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(Modifier.height(6.dp))
        AppHorDivider()
        Spacer(Modifier.height(6.dp))

        Row {
            Text(text = "Порывы ветра", style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.weight(1f))
            Text(text = "$windMaxSpeed км/ч", style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(Modifier.height(6.dp))
        AppHorDivider()
        Spacer(Modifier.height(6.dp))

        Row {
            Text(text = "Направление", style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.weight(1f))
            Text(text = windDirection, style = MaterialTheme.typography.bodyLarge)
        }
    }
}