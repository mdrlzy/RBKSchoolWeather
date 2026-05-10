package com.mdrlzy.rbkweather.presentation.home.model

sealed interface HomeEffect {
    data object WeatherLoadFailed : HomeEffect

    data object RequestLocationPermission : HomeEffect

    data object LocationPermissionDenied : HomeEffect
}