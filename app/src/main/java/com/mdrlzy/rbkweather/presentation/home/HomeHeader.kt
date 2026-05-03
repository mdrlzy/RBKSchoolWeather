package com.mdrlzy.rbkweather.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mdrlzy.ui.theme.OnWeatherDescription

@Composable
fun HomeHeader(state: HomeWeatherPageUiState) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "РАБОТА",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White
        )

        Text(
            text = state.city,
            style = MaterialTheme.typography.displayMedium,
            color = Color.White
        )

        Text(
            text = "${state.temp}°",
            style = MaterialTheme.typography.displayLarge,
            color = Color.White
        )

        Text(
            text = state.description,
            style = MaterialTheme.typography.labelMedium,
            color = OnWeatherDescription
        )

        Text(
            text = "Макс.: ${state.maxTemp}°, мин.: ${state.minTemp}°",
            style = MaterialTheme.typography.labelMedium,
            color = Color.White.copy(alpha = 0.7f)
        )
    }
}