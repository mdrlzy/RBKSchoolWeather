package com.mdrlzy.rbkweather.presentation.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mdrlzy.ui.components.AppHorDivider
import com.mdrlzy.ui.components.IconTitle
import com.mdrlzy.ui.components.InfoCard
import com.mdrlzy.rbkweather.presentation.home.model.DailyForecastUi
import com.mdrlzy.ui.theme.CoreRDrawable
import com.mdrlzy.ui.theme.CoreRString
import com.mdrlzy.ui.theme.OnCardContent

@Composable
fun DailyForecastCard(
    dailyItems: List<DailyForecastUi>,
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
        Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp)) {
            IconTitle(
                icon = CoreRDrawable.calendar,
                text = stringResource(CoreRString.ten_day_forecast)
            )

            Spacer(Modifier.height(12.dp))
            AppHorDivider()
            Spacer(Modifier.height(12.dp))

            dailyItems.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = if (item.isToday) stringResource(CoreRString.today) else item.day,
                        color = OnCardContent,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.width(96.dp),
                    )
                    Icon(
                        painter = painterResource(CoreRDrawable.sun),
                        contentDescription = null,
                        tint = Color(0xFFFFD45E),
                    )
                    Spacer(Modifier.width(18.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(
                                CoreRString.temperature_with_unit,
                                item.minTemp,
                                temperatureUnit,
                            ),
                            color = OnCardContent.copy(alpha = 0.52f),
                            style = MaterialTheme.typography.labelMedium,
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 6.dp)
                                .height(4.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0x14000000))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.45f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFF60DACF))
                            )
                        }
                        Text(
                            text = stringResource(
                                CoreRString.temperature_with_unit,
                                item.maxTemp,
                                temperatureUnit,
                            ),
                            color = OnCardContent,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                }

                if (index != dailyItems.lastIndex) {
                    Spacer(Modifier.height(12.dp))
                    AppHorDivider()
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}
