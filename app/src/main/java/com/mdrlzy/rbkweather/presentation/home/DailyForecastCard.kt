package com.mdrlzy.rbkweather.presentation.home

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
import com.mdrlzy.ui.theme.CoreRDrawable
import com.mdrlzy.ui.theme.CoreRString
import com.mdrlzy.ui.theme.OnCardContent

private data class DailyForecastUi(
    val day: String,
    val minTemp: Int,
    val maxTemp: Int,
)

private val dailyForecastMock = listOf(
    DailyForecastUi(day = "Сегодня", minTemp = 5, maxTemp = 11),
    DailyForecastUi(day = "Сб", minTemp = 2, maxTemp = 9),
    DailyForecastUi(day = "Вс", minTemp = 0, maxTemp = 9),
    DailyForecastUi(day = "Пн", minTemp = -2, maxTemp = 9),
    DailyForecastUi(day = "Вт", minTemp = -4, maxTemp = -1),
    DailyForecastUi(day = "Ср", minTemp = -4, maxTemp = 0),
    DailyForecastUi(day = "Чт", minTemp = -4, maxTemp = 1),
    DailyForecastUi(day = "Пт", minTemp = -4, maxTemp = 1),
    DailyForecastUi(day = "Сб", minTemp = -3, maxTemp = 2),
    DailyForecastUi(day = "Вс", minTemp = -2, maxTemp = 2),
)

@Composable
fun DailyForecastCard() {
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

            dailyForecastMock.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = item.day,
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
                            text = "${item.minTemp}°",
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
                            text = "${item.maxTemp}°",
                            color = OnCardContent,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                }

                if (index != dailyForecastMock.lastIndex) {
                    Spacer(Modifier.height(12.dp))
                    AppHorDivider()
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}
