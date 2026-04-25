@file:OptIn(ExperimentalMaterial3Api::class)

package com.mdrlzy.rbkweather.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mdrlzy.ui.theme.CoreRDrawable

@Composable
fun HomeScreen(
    state: HomeScreenState,
    onRefresh: () -> Unit
) {
    val pullRefreshState = rememberPullToRefreshState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullToRefresh(
                state = pullRefreshState,
                isRefreshing = state.isRefreshing,
                onRefresh = onRefresh
            )
    ) {

        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(CoreRDrawable.bg_clear_day),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(
                start = 18.dp,
                end = 18.dp,
                top = 72.dp,
                bottom = 40.dp
            )
        ) {

            item {
                HomeHeader(state)
                Spacer(Modifier.height(68.dp))
            }

            item {
                HourlyCard()
            }

            item {
                DailyForecastCard()
            }

            item {
                Row {
                    Average(Modifier.weight(1f))
                    Spacer(Modifier.width(8.dp))
                    FeelsLike(Modifier.weight(1f))
                }
            }

            item {
                WindCard()
            }

            item {
                Row {
                    Humidity(Modifier.weight(1f))
                    Spacer(Modifier.width(8.dp))
                    Pressure(Modifier.weight(1f))
                }
            }

            item {
                Row {
                    UVIndex(Modifier.weight(1f))
                    Spacer(Modifier.width(8.dp))
                    Sunset(Modifier.weight(1f))
                }
            }
        }
    }
}
