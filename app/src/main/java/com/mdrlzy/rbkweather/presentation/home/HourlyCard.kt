package com.mdrlzy.rbkweather.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mdrlzy.ui.components.AppHorDivider
import com.mdrlzy.ui.components.InfoCard
import com.mdrlzy.ui.theme.CoreRDrawable
import com.mdrlzy.ui.theme.OnCardContent

data class HourlyUiModel(
    val hour: String,
    val temperature: String,
)

private val mockHourlyItems = listOf(
    HourlyUiModel(hour = "Сейчас", temperature = "10°"),
    HourlyUiModel(hour = "17:00", temperature = "9°"),
    HourlyUiModel(hour = "18:00", temperature = "8°"),
    HourlyUiModel(hour = "19:00", temperature = "7°"),
    HourlyUiModel(hour = "20:00", temperature = "6°"),
    HourlyUiModel(hour = "19:00", temperature = "7°"),
    HourlyUiModel(hour = "20:00", temperature = "6°"),
)

@Composable
fun HourlyCard(
    description: String,
    hourItems: List<HourlyUiModel>,
) {
    InfoCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(
                text = description,
                color = OnCardContent,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(12.dp))

            AppHorDivider()

            Spacer(Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(hourItems) { item ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = item.hour,
                            style = MaterialTheme.typography.bodyLarge,
                            color = OnCardContent
                        )
                        Spacer(Modifier.height(12.dp))
                        Icon(
                            painter = painterResource(CoreRDrawable.sun),
                            contentDescription = null,
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            item.temperature,
                            style = MaterialTheme.typography.bodyLarge,
                            color = OnCardContent
                        )
                    }
                }
            }
        }
    }
}