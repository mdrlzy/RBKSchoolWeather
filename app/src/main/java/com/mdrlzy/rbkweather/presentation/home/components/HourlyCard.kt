package com.mdrlzy.rbkweather.presentation.home.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mdrlzy.rbkweather.presentation.home.model.HourlyUiModel
import com.mdrlzy.ui.components.AppHorDivider
import com.mdrlzy.ui.components.InfoCard
import com.mdrlzy.ui.theme.CoreRDrawable
import com.mdrlzy.ui.theme.CoreRString
import com.mdrlzy.ui.theme.OnCardContent

@Composable
fun HourlyCard(
    description: String,
    hourItems: List<HourlyUiModel>,
    isCelciusNotFarenheit: Boolean,
) {
    val temperatureUnit = stringResource(
        if (isCelciusNotFarenheit) {
            CoreRString.degrees_celsius_symbol
        } else {
            CoreRString.degrees_fahrenheit_symbol
        }
    )

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
                            text = if (item.isCurrent) stringResource(CoreRString.now) else item.hour,
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
                            stringResource(
                                CoreRString.temperature_with_unit,
                                item.temperature,
                                temperatureUnit,
                            ),
                            style = MaterialTheme.typography.bodyLarge,
                            color = OnCardContent
                        )
                    }
                }
            }
        }
    }
}