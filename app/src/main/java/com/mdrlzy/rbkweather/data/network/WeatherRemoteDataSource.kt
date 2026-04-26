package com.mdrlzy.rbkweather.data.network

import com.mdrlzy.rbkweather.BuildConfig
import com.mdrlzy.rbkweather.data.network.dto.OneCallResponseDto
import com.mdrlzy.rbkweather.domain.model.LocationData

interface WeatherRemoteDataSource {
    suspend fun getCurrentWeather(locationData: LocationData): OneCallResponseDto
}

class WeatherRemoteDataSourceImpl(
    private val api: WeatherApi,
) : WeatherRemoteDataSource {
    override suspend fun getCurrentWeather(locationData: LocationData): OneCallResponseDto {
        return api.getOneCall(
            lat = locationData.latitude,
            lon = locationData.longitude,
            apiKey = BuildConfig.WEATHER_API_KEY,
        )
    }
}
