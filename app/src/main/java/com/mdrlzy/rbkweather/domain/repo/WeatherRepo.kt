package com.mdrlzy.rbkweather.domain.repo

import com.mdrlzy.rbkweather.domain.model.LocationData
import com.mdrlzy.rbkweather.domain.model.Weather

interface WeatherRepo {
    suspend fun getCurrent(
        locationData: LocationData
    ): Result<Weather>
}