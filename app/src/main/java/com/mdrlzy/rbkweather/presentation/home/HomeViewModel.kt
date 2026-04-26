package com.mdrlzy.rbkweather.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdrlzy.rbkweather.domain.model.OneCallWeather
import com.mdrlzy.rbkweather.domain.usecase.GetCurrentWeatherUseCase
import com.mdrlzy.rbkweather.presentation.location.LocationPermissionHelper
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt

sealed interface HomeEffect {
    data object RequestLocationPermission : HomeEffect
    data object WeatherLoadFailed : HomeEffect
    data object LocationPermissionDenied : HomeEffect
}

class HomeViewModel(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val locationPermissionHelper: LocationPermissionHelper,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeScreenState())
    val state: StateFlow<HomeScreenState> = _state.asStateFlow()

    private val _effect = Channel<HomeEffect>(Channel.BUFFERED)
    val effect: Flow<HomeEffect> = _effect.receiveAsFlow()

    init {
        onRefresh()
    }

    fun onRefresh() {
        if (locationPermissionHelper.hasFineLocationPermission()) {
            loadWeather()
        } else {
            requestLocationPermission()
        }
    }

    fun onLocationPermissionResult(granted: Boolean) {
        if (granted) {
            loadWeather()
        } else {
            emitEffect(HomeEffect.LocationPermissionDenied)
        }
    }

    private fun requestLocationPermission() {
        viewModelScope.launch {
            _effect.send(HomeEffect.RequestLocationPermission)
        }
    }

    private fun emitEffect(effect: HomeEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    override fun onCleared() {
        _effect.close()
        super.onCleared()
    }

    private fun loadWeather() {
        if (_state.value.isRefreshing) return

        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }

            val homeStateResult = getCurrentWeatherUseCase()
            homeStateResult.fold(
                onSuccess = { weather ->
                    val newState = weather.toHomeScreenState()
                    _state.value = newState.copy(isRefreshing = false, isInitialized = true)
                },
                onFailure = {
                    _state.update {
                        it.copy(isRefreshing = false)
                    }
                    emitEffect(HomeEffect.WeatherLoadFailed)
                }
            )
        }
    }
}

private fun OneCallWeather.toHomeScreenState(): HomeScreenState {
    val hourFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val dayFormatter = DateTimeFormatter.ofPattern("E", Locale("ru"))

    val hourlyItems = buildList {
        add(
            HourlyUiModel(
                hour = "Сейчас",
                temperature = "${current.temp.roundToInt()}°",
            )
        )
        hourly.take(11).forEach { item ->
            add(
                HourlyUiModel(
                    hour = item.dateTime.format(hourFormatter),
                    temperature = "${item.temp.roundToInt()}°",
                )
            )
        }
    }

    val dailyItems = daily.take(8).mapIndexed { index, item ->
        DailyForecastUi(
            day = if (index == 0) "Сегодня" else item.dateTime
                .format(dayFormatter)
                .replaceFirstChar { it.uppercase() },
            minTemp = item.temp.min.roundToInt(),
            maxTemp = item.temp.max.roundToInt(),
        )
    }

    val firstDaily = daily.firstOrNull()
    val sunriseTime = current.sunrise ?: firstDaily?.sunrise ?: current.dateTime
    val sunsetTime = current.sunset ?: firstDaily?.sunset ?: current.dateTime

    val shortDescription = current.weather.firstOrNull()?.description?.takeIf { it.isNotBlank() }
        ?: firstDaily?.weather?.firstOrNull()?.description
        ?: ""

    return HomeScreenState(
        city = timezone.substringAfterLast('/').replace('_', ' '),
        description = shortDescription,
        detailedDescription = shortDescription,
        hourlyItems = hourlyItems,
        dailyItems = dailyItems,
        temp = current.temp.roundToInt(),
        minTemp = firstDaily?.temp?.min?.roundToInt() ?: current.temp.roundToInt(),
        maxTemp = firstDaily?.temp?.max?.roundToInt() ?: current.temp.roundToInt(),
        feelsLike = current.feelsLike.roundToInt(),
        humidity = current.humidity,
        pressure = current.pressure,
        uvIndex = current.uvIndex.roundToInt(),
        windSpeed = current.windSpeed.roundToInt(),
        windDirection = current.windDeg.toCardinalDirection(),
        windMaxSpeed = daily.maxOfOrNull { it.windSpeed }?.roundToInt() ?: current.windSpeed.roundToInt(),
        sunsetTime = sunsetTime,
        sunriseTime = sunriseTime,
        isRefreshing = false,
    )
}

private fun Int.toCardinalDirection(): String {
    val directions = listOf("С", "СВ", "В", "ЮВ", "Ю", "ЮЗ", "З", "СЗ")
    val index = ((this % 360 + 22.5) / 45).toInt() % directions.size
    return directions[index]
}
