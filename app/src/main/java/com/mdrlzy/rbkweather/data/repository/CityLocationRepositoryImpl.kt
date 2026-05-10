package com.mdrlzy.rbkweather.data.repository

import com.mdrlzy.rbkweather.data.local.dao.CityLocationDao
import com.mdrlzy.rbkweather.data.local.dao.DailyWeatherDao
import com.mdrlzy.rbkweather.data.local.dao.WeatherCacheDao
import com.mdrlzy.rbkweather.data.local.dao.WeatherConditionDao
import com.mdrlzy.rbkweather.data.local.entity.CityLocationEntity
import com.mdrlzy.rbkweather.domain.model.CityLocation
import com.mdrlzy.rbkweather.domain.model.CityWeatherSummary
import com.mdrlzy.rbkweather.domain.repository.CityLocationRepository

private val DEFAULT_CITIES = listOf(
    CityLocationEntity(
        name = "Астана",
        latitude = 51.1282,
        longitude = 71.4304,
    ),
    CityLocationEntity(
        name = "Алматы",
        latitude = 43.2383,
        longitude = 76.9456,
    ),
)

class CityLocationRepositoryImpl(
    private val cityLocationDao: CityLocationDao,
    private val weatherCacheDao: WeatherCacheDao,
    private val dailyWeatherDao: DailyWeatherDao,
    private val weatherConditionDao: WeatherConditionDao,
) : CityLocationRepository {

    override suspend fun addCity(cityLocation: CityLocation) {
        cityLocationDao.insertCity(cityLocation.toEntity())
    }

    override suspend fun getCities(): List<CityLocation> {
        return cityLocationDao.getCities().map { it.toDomain() }
    }

    override suspend fun getCityWeatherSummaries(): List<CityWeatherSummary> {
        return cityLocationDao.getCities().map { city ->
            val cache = weatherCacheDao.getCache(city.id)
            val firstDaily = dailyWeatherDao.getFirstDaily(city.id)
            val currentCondition = weatherConditionDao.getCurrentCondition(city.id)

            CityWeatherSummary(
                id = city.id,
                cityName = city.name,
                timezoneOffsetSeconds = cache?.timezoneOffsetSeconds,
                currentDateTimeEpochSeconds = cache?.currentDateTimeEpochSeconds,
                currentTemp = cache?.currentTemp,
                currentConditionDescription = currentCondition?.description,
                minTemp = firstDaily?.minTemp,
                maxTemp = firstDaily?.maxTemp,
            )
        }
    }

    override suspend fun ensureDefaultCities() {
        DEFAULT_CITIES.forEach { city ->
            val existingCity = cityLocationDao.getCityByCoordinates(
                latitude = city.latitude,
                longitude = city.longitude,
                coordinateTolerance = 0.0001,
            )
            if (existingCity == null) {
                cityLocationDao.insertCity(city)
            }
        }
    }
}

private fun CityLocation.toEntity(): CityLocationEntity {
    return CityLocationEntity(
        id = id,
        name = name,
        latitude = latitude,
        longitude = longitude,
    )
}

private fun CityLocationEntity.toDomain(): CityLocation {
    return CityLocation(
        id = id,
        name = name,
        latitude = latitude,
        longitude = longitude,
    )
}
