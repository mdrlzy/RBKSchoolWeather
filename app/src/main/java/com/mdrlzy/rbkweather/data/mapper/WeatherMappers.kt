package com.mdrlzy.rbkweather.data.mapper

import com.mdrlzy.rbkweather.data.local.entity.DailyWeatherEntity
import com.mdrlzy.rbkweather.data.local.entity.HourlyWeatherEntity
import com.mdrlzy.rbkweather.data.local.entity.WeatherCacheEntity
import com.mdrlzy.rbkweather.data.local.entity.WeatherConditionEntity
import com.mdrlzy.rbkweather.data.network.dto.CurrentDto
import com.mdrlzy.rbkweather.data.network.dto.DailyForecastDto
import com.mdrlzy.rbkweather.data.network.dto.DailyTempDto
import com.mdrlzy.rbkweather.data.network.dto.HourlyForecastDto
import com.mdrlzy.rbkweather.data.network.dto.OneCallResponseDto
import com.mdrlzy.rbkweather.data.network.dto.WeatherInfoDto
import com.mdrlzy.rbkweather.domain.model.CurrentWeather
import com.mdrlzy.rbkweather.domain.model.DailyTemp
import com.mdrlzy.rbkweather.domain.model.DailyWeather
import com.mdrlzy.rbkweather.domain.model.HourlyWeather
import com.mdrlzy.rbkweather.domain.model.OneCallWeather
import com.mdrlzy.rbkweather.domain.model.WeatherCondition
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

private const val OWNER_CURRENT = "current"
private const val OWNER_HOURLY = "hourly"
private const val OWNER_DAILY = "daily"

private data class OwnerKey(
    val ownerType: String,
    val ownerIndex: Int,
)

data class LocalWeatherBundle(
    val cache: WeatherCacheEntity,
    val hourly: List<HourlyWeatherEntity>,
    val daily: List<DailyWeatherEntity>,
    val conditions: List<WeatherConditionEntity>,
)

fun OneCallResponseDto.toDomain(): OneCallWeather {
    return OneCallWeather(
        latitude = lat,
        longitude = lon,
        timezone = timezone,
        timezoneOffsetSeconds = timezone_offset,
        current = current.toDomain(timezone_offset),
        hourly = hourly.map { it.toDomain(timezone_offset) },
        daily = daily.map { it.toDomain(timezone_offset) },
    )
}

fun OneCallWeather.toLocalBundle(): LocalWeatherBundle {
    val currentEpoch = current.dateTime.toEpochSecond()

    val cache = WeatherCacheEntity(
        id = 0,
        cachedAtEpochMillis = System.currentTimeMillis(),
        latitude = latitude,
        longitude = longitude,
        timezone = timezone,
        timezoneOffsetSeconds = timezoneOffsetSeconds,
        currentDateTimeEpochSeconds = currentEpoch,
        currentSunriseEpochSeconds = current.sunrise?.toEpochSecond(),
        currentSunsetEpochSeconds = current.sunset?.toEpochSecond(),
        currentTemp = current.temp,
        currentFeelsLike = current.feelsLike,
        currentPressure = current.pressure,
        currentHumidity = current.humidity,
        currentUvIndex = current.uvIndex,
        currentWindSpeed = current.windSpeed,
        currentWindDeg = current.windDeg,
    )

    val hourlyEntities = hourly.mapIndexed { index, item ->
        HourlyWeatherEntity(
            position = index,
            dateTimeEpochSeconds = item.dateTime.toEpochSecond(),
            temp = item.temp,
            precipitationProbability = item.precipitationProbability,
        )
    }

    val dailyEntities = daily.mapIndexed { index, item ->
        DailyWeatherEntity(
            position = index,
            dateTimeEpochSeconds = item.dateTime.toEpochSecond(),
            sunriseEpochSeconds = item.sunrise?.toEpochSecond(),
            sunsetEpochSeconds = item.sunset?.toEpochSecond(),
            minTemp = item.temp.min,
            maxTemp = item.temp.max,
            dayTemp = item.temp.day,
            windSpeed = item.windSpeed,
            windDeg = item.windDeg,
            uvIndex = item.uvIndex,
            summary = item.summary,
        )
    }

    val currentConditions = current.weather.mapIndexed { index, weather ->
        weather.toEntity(ownerType = OWNER_CURRENT, ownerIndex = 0, position = index)
    }
    val hourlyConditions = hourly.flatMapIndexed { hourIndex, item ->
        item.weather.mapIndexed { conditionIndex, weather ->
            weather.toEntity(ownerType = OWNER_HOURLY, ownerIndex = hourIndex, position = conditionIndex)
        }
    }
    val dailyConditions = daily.flatMapIndexed { dayIndex, item ->
        item.weather.mapIndexed { conditionIndex, weather ->
            weather.toEntity(ownerType = OWNER_DAILY, ownerIndex = dayIndex, position = conditionIndex)
        }
    }

    return LocalWeatherBundle(
        cache = cache,
        hourly = hourlyEntities,
        daily = dailyEntities,
        conditions = currentConditions + hourlyConditions + dailyConditions,
    )
}

fun WeatherCacheEntity.toDomain(
    hourlyEntities: List<HourlyWeatherEntity>,
    dailyEntities: List<DailyWeatherEntity>,
    conditionEntities: List<WeatherConditionEntity>,
): OneCallWeather {
    val conditionsByOwner = conditionEntities.groupBy { OwnerKey(it.ownerType, it.ownerIndex) }
    val offset = timezoneOffsetSeconds

    val currentConditions = conditionsByOwner[OwnerKey(OWNER_CURRENT, 0)]
        .orEmpty()
        .sortedBy { it.position }
        .map { it.toDomain() }

    return OneCallWeather(
        latitude = latitude,
        longitude = longitude,
        timezone = timezone,
        timezoneOffsetSeconds = timezoneOffsetSeconds,
        current = CurrentWeather(
            dateTime = currentDateTimeEpochSeconds.toOffsetDateTime(offset),
            sunrise = currentSunriseEpochSeconds?.toOffsetDateTime(offset),
            sunset = currentSunsetEpochSeconds?.toOffsetDateTime(offset),
            temp = currentTemp,
            feelsLike = currentFeelsLike,
            pressure = currentPressure,
            humidity = currentHumidity,
            uvIndex = currentUvIndex,
            windSpeed = currentWindSpeed,
            windDeg = currentWindDeg,
            weather = currentConditions,
        ),
        hourly = hourlyEntities.mapIndexed { hourIndex, item ->
            val key = OwnerKey(OWNER_HOURLY, hourIndex)
            HourlyWeather(
                dateTime = item.dateTimeEpochSeconds.toOffsetDateTime(offset),
                temp = item.temp,
                precipitationProbability = item.precipitationProbability,
                weather = conditionsByOwner[key].orEmpty().sortedBy { it.position }.map { it.toDomain() },
            )
        },
        daily = dailyEntities.mapIndexed { dayIndex, item ->
            val key = OwnerKey(OWNER_DAILY, dayIndex)
            DailyWeather(
                dateTime = item.dateTimeEpochSeconds.toOffsetDateTime(offset),
                sunrise = item.sunriseEpochSeconds?.toOffsetDateTime(offset),
                sunset = item.sunsetEpochSeconds?.toOffsetDateTime(offset),
                temp = DailyTemp(
                    min = item.minTemp,
                    max = item.maxTemp,
                    day = item.dayTemp,
                ),
                windSpeed = item.windSpeed,
                windDeg = item.windDeg,
                uvIndex = item.uvIndex,
                summary = item.summary,
                weather = conditionsByOwner[key].orEmpty().sortedBy { it.position }.map { it.toDomain() },
            )
        },
    )
}

private fun CurrentDto.toDomain(timezoneOffsetSeconds: Int): CurrentWeather {
    return CurrentWeather(
        dateTime = dt.toOffsetDateTime(timezoneOffsetSeconds),
        sunrise = sunrise?.toOffsetDateTime(timezoneOffsetSeconds),
        sunset = sunset?.toOffsetDateTime(timezoneOffsetSeconds),
        temp = temp,
        feelsLike = feels_like,
        pressure = pressure,
        humidity = humidity,
        uvIndex = uvi,
        windSpeed = wind_speed,
        windDeg = wind_deg,
        weather = weather.map { it.toDomain() },
    )
}

private fun HourlyForecastDto.toDomain(timezoneOffsetSeconds: Int): HourlyWeather {
    return HourlyWeather(
        dateTime = dt.toOffsetDateTime(timezoneOffsetSeconds),
        temp = temp,
        precipitationProbability = pop,
        weather = weather.map { it.toDomain() },
    )
}

private fun DailyForecastDto.toDomain(timezoneOffsetSeconds: Int): DailyWeather {
    return DailyWeather(
        dateTime = dt.toOffsetDateTime(timezoneOffsetSeconds),
        sunrise = sunrise?.toOffsetDateTime(timezoneOffsetSeconds),
        sunset = sunset?.toOffsetDateTime(timezoneOffsetSeconds),
        temp = temp.toDomain(),
        windSpeed = wind_speed,
        windDeg = wind_deg,
        uvIndex = uvi,
        summary = summary,
        weather = weather.map { it.toDomain() },
    )
}

private fun DailyTempDto.toDomain(): DailyTemp {
    return DailyTemp(
        min = min,
        max = max,
        day = day,
    )
}

private fun WeatherInfoDto.toDomain(): WeatherCondition {
    return WeatherCondition(
        id = id,
        main = main,
        description = description,
        icon = icon,
    )
}

private fun WeatherCondition.toEntity(
    ownerType: String,
    ownerIndex: Int,
    position: Int,
): WeatherConditionEntity {
    return WeatherConditionEntity(
        ownerType = ownerType,
        ownerIndex = ownerIndex,
        position = position,
        weatherId = id,
        main = main,
        description = description,
        icon = icon,
    )
}

private fun WeatherConditionEntity.toDomain(): WeatherCondition {
    return WeatherCondition(
        id = weatherId,
        main = main,
        description = description,
        icon = icon,
    )
}

private fun Long.toOffsetDateTime(timezoneOffsetSeconds: Int): OffsetDateTime {
    return OffsetDateTime.ofInstant(
        Instant.ofEpochSecond(this),
        ZoneOffset.ofTotalSeconds(timezoneOffsetSeconds),
    )
}
