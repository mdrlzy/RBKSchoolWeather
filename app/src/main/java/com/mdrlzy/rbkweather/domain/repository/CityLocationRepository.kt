package com.mdrlzy.rbkweather.domain.repository

import com.mdrlzy.rbkweather.domain.model.CityLocation
import com.mdrlzy.rbkweather.domain.model.CityWeatherSummary

interface CityLocationRepository {
    suspend fun getCities(): List<CityLocation>

    suspend fun getCityWeatherSummaries(): List<CityWeatherSummary>

    suspend fun ensureDefaultCities()
}
