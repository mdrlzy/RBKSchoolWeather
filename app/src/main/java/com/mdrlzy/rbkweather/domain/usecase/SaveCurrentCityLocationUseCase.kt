package com.mdrlzy.rbkweather.domain.usecase

import com.mdrlzy.rbkweather.domain.model.CityLocation
import com.mdrlzy.rbkweather.domain.repository.CityLocationRepository
import com.mdrlzy.rbkweather.domain.repository.LocationRepository

class SaveCurrentCityLocationUseCase(
    private val locationRepository: LocationRepository,
    private val cityLocationRepository: CityLocationRepository
) {
    suspend operator fun invoke(): Boolean {
        val currentLocation = locationRepository.getCurrentLocation() ?: return false
        val currentCity = locationRepository.getCityName(currentLocation)
        val currentCityLocation = CityLocation(
            id = 0,
            name = currentCity,
            latitude = currentLocation.latitude,
            longitude = currentLocation.longitude
        )

        cityLocationRepository.addCity(currentCityLocation)
        return true
    }
}