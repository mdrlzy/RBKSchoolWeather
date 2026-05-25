package com.mdrlzy.rbkweather.data.local

import androidx.room.withTransaction
import com.mdrlzy.rbkweather.data.local.dao.CityLocationDao
import com.mdrlzy.rbkweather.data.local.dao.DailyWeatherDao
import com.mdrlzy.rbkweather.data.local.dao.HourlyWeatherDao
import com.mdrlzy.rbkweather.data.local.dao.WeatherCacheDao
import com.mdrlzy.rbkweather.data.local.dao.WeatherConditionDao
import com.mdrlzy.rbkweather.data.local.entity.CityLocationEntity
import com.mdrlzy.rbkweather.data.mapper.toDomain
import com.mdrlzy.rbkweather.data.mapper.toEntity
import com.mdrlzy.rbkweather.data.mapper.toLocalBundle
import com.mdrlzy.rbkweather.domain.model.CityLocation
import com.mdrlzy.rbkweather.domain.model.LocationData
import com.mdrlzy.rbkweather.domain.model.OneCallWeather
import com.mdrlzy.rbkweather.domain.repository.LocationRepository

interface WeatherLocalDataSource {
    suspend fun getCurrentWeather(cityLocation: CityLocation): OneCallWeather?
    suspend fun saveCurrentWeather(
        cityLocation: CityLocation,
        weather: OneCallWeather,
    )
}

class WeatherLocalDataSourceImpl(
    private val weatherDatabase: WeatherDatabase,
    private val cityLocationDao: CityLocationDao,
    private val weatherCacheDao: WeatherCacheDao,
    private val hourlyWeatherDao: HourlyWeatherDao,
    private val dailyWeatherDao: DailyWeatherDao,
    private val weatherConditionDao: WeatherConditionDao,
    private val locationRepository: LocationRepository,
) : WeatherLocalDataSource {
    override suspend fun getCurrentWeather(cityLocation: CityLocation): OneCallWeather? {
        val locationId = cityLocationDao.getCityByCoordinates(
            latitude = cityLocation.latitude,
            longitude = cityLocation.longitude,
        )?.id ?: return null
        val cache = weatherCacheDao.getCache(locationId) ?: return null
        val hourly = hourlyWeatherDao.getHourly(cache.locationId)
        val daily = dailyWeatherDao.getDaily(cache.locationId)
        val conditions = weatherConditionDao.getConditions(cache.locationId)

        return cache.toDomain(hourly, daily, conditions)
    }

    override suspend fun saveCurrentWeather(
        cityLocation: CityLocation,
        weather: OneCallWeather,
    ) {
        val locationId = resolveLocationId(cityLocation)
        val localBundle = weather.toLocalBundle(locationId)

        weatherDatabase.withTransaction {
            weatherCacheDao.clearCache(locationId)
            weatherCacheDao.upsertCache(localBundle.cache)
            val hourlyIds = hourlyWeatherDao.insertHourly(localBundle.hourly)
            val dailyIds = dailyWeatherDao.insertDaily(localBundle.daily)
            val hourlyConditions = hourlyIds.zip(weather.hourly).flatMap { (hourlyId, item) ->
                item.weather.map { condition ->
                    condition.toEntity(hourlyWeatherId = hourlyId)
                }
            }
            val dailyConditions = dailyIds.zip(weather.daily).flatMap { (dailyId, item) ->
                item.weather.map { condition ->
                    condition.toEntity(dailyWeatherId = dailyId)
                }
            }
            weatherConditionDao.insertConditions(
                localBundle.currentConditions + hourlyConditions + dailyConditions,
            )
        }
    }

    private suspend fun resolveLocationId(cityLocation: CityLocation): Long {
        return cityLocationDao.getCityByCoordinates(
            latitude = cityLocation.latitude,
            longitude = cityLocation.longitude,
        )?.id ?: cityLocationDao.insertCity(
            CityLocationEntity(
                name = cityLocation.name ?: locationRepository.getCityName(cityLocation.toLocationData()),
                latitude = cityLocation.latitude,
                longitude = cityLocation.longitude,
            ),
        )
    }
}

private fun CityLocation.toLocationData(): LocationData {
    return LocationData(
        latitude = latitude,
        longitude = longitude,
    )
}
