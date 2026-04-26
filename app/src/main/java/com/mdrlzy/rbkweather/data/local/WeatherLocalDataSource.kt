package com.mdrlzy.rbkweather.data.local

import androidx.room.withTransaction
import com.mdrlzy.rbkweather.data.local.dao.DailyWeatherDao
import com.mdrlzy.rbkweather.data.local.dao.HourlyWeatherDao
import com.mdrlzy.rbkweather.data.local.dao.WeatherCacheDao
import com.mdrlzy.rbkweather.data.local.dao.WeatherConditionDao
import com.mdrlzy.rbkweather.data.mapper.toDomain
import com.mdrlzy.rbkweather.data.mapper.toLocalBundle
import com.mdrlzy.rbkweather.domain.model.OneCallWeather

interface WeatherLocalDataSource {
    suspend fun getCurrentWeather(): OneCallWeather?
    suspend fun saveCurrentWeather(weather: OneCallWeather)
}

class WeatherLocalDataSourceImpl(
    private val weatherDatabase: WeatherDatabase,
    private val weatherCacheDao: WeatherCacheDao,
    private val hourlyWeatherDao: HourlyWeatherDao,
    private val dailyWeatherDao: DailyWeatherDao,
    private val weatherConditionDao: WeatherConditionDao,
) : WeatherLocalDataSource {
    override suspend fun getCurrentWeather(): OneCallWeather? {
        val cache = weatherCacheDao.getCache() ?: return null
        val hourly = hourlyWeatherDao.getHourly()
        val daily = dailyWeatherDao.getDaily()
        val conditions = weatherConditionDao.getConditions()

        return cache.toDomain(hourly, daily, conditions)
    }

    override suspend fun saveCurrentWeather(weather: OneCallWeather) {
        val localBundle = weather.toLocalBundle()

        weatherDatabase.withTransaction {
            weatherCacheDao.clearCache()
            hourlyWeatherDao.clearHourly()
            dailyWeatherDao.clearDaily()
            weatherConditionDao.clearConditions()
            weatherCacheDao.upsertCache(localBundle.cache)
            hourlyWeatherDao.upsertHourly(localBundle.hourly)
            dailyWeatherDao.upsertDaily(localBundle.daily)
            weatherConditionDao.upsertConditions(localBundle.conditions)
        }
    }
}
