package com.mdrlzy.rbkweather.domain.usecase

import com.mdrlzy.rbkweather.domain.model.OneCallWeather
import com.mdrlzy.rbkweather.domain.repository.LocationRepository
import com.mdrlzy.rbkweather.domain.repository.WeatherRepository

class GetCurrentWeatherUseCase(
    private val weatherRepository: WeatherRepository,
    private val locationRepository: LocationRepository,
) {
    suspend operator fun invoke(): Result<OneCallWeather> {
        val locationData = locationRepository.getCurrentLocation()
            ?: return Result.failure(IllegalStateException("No location"))

        return weatherRepository.getCurrent(locationData)
    }
}