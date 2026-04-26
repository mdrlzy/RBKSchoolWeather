package com.mdrlzy.rbkweather.data.repository

import com.mdrlzy.rbkweather.data.local.WeatherLocalDataSource
import com.mdrlzy.rbkweather.data.mapper.toDomain
import com.mdrlzy.rbkweather.data.remote.WeatherRemoteDataSource
import com.mdrlzy.rbkweather.domain.model.LocationData
import com.mdrlzy.rbkweather.domain.model.OneCallWeather
import com.mdrlzy.rbkweather.domain.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.OffsetDateTime
import java.time.ZoneOffset
import kotlin.time.Duration.Companion.days

private val CACHE_MAX_AGE_SECONDS = 1.days.inWholeSeconds

class WeatherRepositoryImpl(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val localDataSource: WeatherLocalDataSource,
) : WeatherRepository {
    override suspend fun getCurrent(
        locationData: LocationData
    ): Result<OneCallWeather> = withContext(Dispatchers.IO) {
        val localWeather = localDataSource.getCurrentWeather()
            ?.takeIf { it.isFreshCache() }
        val remoteResult = runCatching { remoteDataSource.getCurrentWeather(locationData).toDomain() }

        remoteResult.fold(
            onSuccess = { remoteWeather ->
                runCatching {
                    localDataSource.saveCurrentWeather(remoteWeather)
                }

                Result.success(remoteWeather)
            },
            onFailure = { error ->
                if (localWeather != null) {
                    Result.success(localWeather)
                } else {
                    Result.failure(error)
                }
            }
        )
    }
}

private fun OneCallWeather.isFreshCache(now: OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC)): Boolean {
    val ageSeconds = now.toEpochSecond() - current.dateTime.toEpochSecond()
    return ageSeconds <= CACHE_MAX_AGE_SECONDS
}