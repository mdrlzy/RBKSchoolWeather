package com.mdrlzy.rbkweather.domain.repository

import com.mdrlzy.rbkweather.domain.model.LocationData
import com.mdrlzy.rbkweather.domain.model.OneCallWeather

interface WeatherRepository {
    suspend fun getCurrent(
        locationData: LocationData
    ): Result<OneCallWeather>
}