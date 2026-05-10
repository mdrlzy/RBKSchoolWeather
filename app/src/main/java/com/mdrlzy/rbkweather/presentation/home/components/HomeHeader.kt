package com.mdrlzy.rbkweather.presentation.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.mdrlzy.rbkweather.presentation.home.model.HomeWeatherPageUiState
import com.mdrlzy.ui.theme.CoreRString
import com.mdrlzy.ui.theme.OnWeatherDescription

@Composable
fun HomeHeader(state: HomeWeatherPageUiState) {
    val maxTemperature = stringResource(CoreRString.temperature_degrees, state.maxTemp)
    val minTemperature = stringResource(CoreRString.temperature_degrees, state.minTemp)

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(CoreRString.work_location),
            style = MaterialTheme.typography.bodySmall,
            color = Color.White
        )

        Text(
            text = state.city,
            style = MaterialTheme.typography.displayMedium,
            color = Color.White
        )

        Text(
            text = stringResource(CoreRString.temperature_degrees, state.temp),
            style = MaterialTheme.typography.displayLarge,
            color = Color.White
        )

        Text(
            text = state.description,
            style = MaterialTheme.typography.labelMedium,
            color = OnWeatherDescription
        )

        Text(
            text = stringResource(CoreRString.max_min_temp, maxTemperature, minTemperature),
            style = MaterialTheme.typography.labelMedium,
            color = Color.White.copy(alpha = 0.7f)
        )
    }
}