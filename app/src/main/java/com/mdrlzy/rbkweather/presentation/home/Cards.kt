package com.mdrlzy.rbkweather.presentation.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mdrlzy.ui.components.AppHorDivider
import com.mdrlzy.ui.components.InfoCardBasic
import com.mdrlzy.ui.components.InfoCardSmall
import com.mdrlzy.ui.theme.CoreRArray
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
            text = stringResource(CoreRString.average_temp_delta, averageTemp),
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
            text = stringResource(CoreRString.temperature_degrees, feelsLike),
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
            text = stringResource(CoreRString.percentage_value, humidity),
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(text = stringResource(CoreRString.humidity_dew_point_now, 5))
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

        Text(text = stringResource(CoreRString.hpa))
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

        Text(text = stringResource(CoreRString.sunrise_at, sunriseTime.format(timeFormatter)))
    }
}

@Composable
fun WindCard(
    windSpeed: Int,
    windMaxSpeed: Int,
    windDirectionDegrees: Int,
) {
    val cardinalDirections = stringArrayResource(CoreRArray.cardinal_directions)
    val windDirection = windDirectionDegrees.toCardinalDirection(cardinalDirections)

    InfoCardBasic(
        icon = CoreRDrawable.wind,
        title = stringResource(CoreRString.wind),
    ) {
        Row {
            Text(text = stringResource(CoreRString.wind_speed), style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.weight(1f))
            Text(
                text = stringResource(
                    CoreRString.value_with_unit,
                    windSpeed,
                    stringResource(CoreRString.km_per_hour),
                ),
                style = MaterialTheme.typography.bodyLarge,
            )
        }

        Spacer(Modifier.height(6.dp))
        AppHorDivider()
        Spacer(Modifier.height(6.dp))

        Row {
            Text(text = stringResource(CoreRString.wind_gusts), style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.weight(1f))
            Text(
                text = stringResource(
                    CoreRString.value_with_unit,
                    windMaxSpeed,
                    stringResource(CoreRString.km_per_hour),
                ),
                style = MaterialTheme.typography.bodyLarge,
            )
        }

        Spacer(Modifier.height(6.dp))
        AppHorDivider()
        Spacer(Modifier.height(6.dp))

        Row {
            Text(text = stringResource(CoreRString.wind_direction), style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.weight(1f))
            Text(text = windDirection, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

private fun Int.toCardinalDirection(directions: Array<String>): String {
    val index = ((this % 360 + 22.5) / 45).toInt() % directions.size
    return directions[index]
}