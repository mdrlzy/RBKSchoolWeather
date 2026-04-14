package com.mdrlzy.rbkweather.domain.repo

import com.mdrlzy.rbkweather.domain.model.Weather

interface WeatherRepo {
    suspend fun getCurrent(
        lat: Double,
        lon: Double,
    ): Result<Weather>
}