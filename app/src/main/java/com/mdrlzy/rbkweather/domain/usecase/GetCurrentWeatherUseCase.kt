package com.mdrlzy.rbkweather.domain.usecase

import com.mdrlzy.rbkweather.domain.model.OneCallWeather
import com.mdrlzy.rbkweather.domain.repo.LocationRepo
import com.mdrlzy.rbkweather.domain.repo.WeatherRepo

class GetCurrentWeatherUseCase(
    private val weatherRepo: WeatherRepo,
    private val locationRepo: LocationRepo,
) {
    suspend operator fun invoke(): Result<OneCallWeather> {
        val locationData = locationRepo.getCurrentLocation()
            ?: return Result.failure(IllegalStateException("No location"))

        return weatherRepo.getCurrent(locationData)
    }
}