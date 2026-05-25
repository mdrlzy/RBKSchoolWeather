package com.mdrlzy.rbkweather.domain.repository

import com.mdrlzy.rbkweather.domain.model.CityLocation
import com.mdrlzy.rbkweather.domain.model.OneCallWeather

interface WeatherRepository {
    suspend fun getCurrent(
        cityLocation: CityLocation
    ): Result<OneCallWeather>
}