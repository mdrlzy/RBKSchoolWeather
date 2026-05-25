@file:OptIn(ExperimentalMaterial3Api::class)

package com.mdrlzy.rbkweather.presentation.home.view

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mdrlzy.rbkweather.presentation.home.components.HomeBottomNavBar
import com.mdrlzy.rbkweather.presentation.home.model.HomeEffect
import com.mdrlzy.rbkweather.presentation.home.model.HomeViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mdrlzy.ui.theme.CoreRDrawable
import com.mdrlzy.ui.theme.CoreRString
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onNavigateToCityList: () -> Unit = {},
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val pullRefreshState = rememberPullToRefreshState()
    val pagerState = rememberPagerState(pageCount = { state.pages.size })
    val weatherLoadFailedMessage = stringResource(CoreRString.home_toast_weather_load_failed)
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.onPermissionLocationResult(granted)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeEffect.WeatherLoadFailed -> {
                    Toast.makeText(
                        context,
                        weatherLoadFailedMessage,
                        Toast.LENGTH_SHORT,
                    ).show()
                }

                HomeEffect.LocationPermissionDenied -> {
                    Toast.makeText(
                        context,
                        CoreRString.home_toast_location_permission_denied,
                        Toast.LENGTH_SHORT,
                    ).show()
                }

                HomeEffect.RequestLocationPermission -> {
                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        }
    }

    if (!state.isInitialized) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullToRefresh(
                state = pullRefreshState,
                isRefreshing = state.isRefreshing,
                onRefresh = { viewModel.onRefresh() }
            )
    ) {

        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(CoreRDrawable.bg_clear_day),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
        )

        if (state.pages.isNotEmpty()) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                beyondViewportPageCount = 1,
            ) { page ->
                HomeWeatherPage(
                    pageState = state.pages[page],
                    isCelciusNotFarenheit = state.isCelciusNotFarenheit,
                )
            }
        }

        HomeBottomNavBar(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            onListClick = onNavigateToCityList,
            currentPage = pagerState.currentPage,
            pageCount = state.pages.size,
        )
    }
}
