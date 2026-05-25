package com.mdrlzy.rbkweather.data.mapper

import com.mdrlzy.rbkweather.data.local.entity.DailyWeatherEntity
import com.mdrlzy.rbkweather.data.local.entity.HourlyWeatherEntity
import com.mdrlzy.rbkweather.data.local.entity.WeatherCacheEntity
import com.mdrlzy.rbkweather.data.local.entity.WeatherConditionEntity
import com.mdrlzy.rbkweather.data.remote.dto.CurrentDto
import com.mdrlzy.rbkweather.data.remote.dto.DailyForecastDto
import com.mdrlzy.rbkweather.data.remote.dto.DailyTempDto
import com.mdrlzy.rbkweather.data.remote.dto.HourlyForecastDto
import com.mdrlzy.rbkweather.data.remote.dto.OneCallResponseDto
import com.mdrlzy.rbkweather.data.remote.dto.WeatherInfoDto
import com.mdrlzy.rbkweather.domain.model.CurrentWeather
import com.mdrlzy.rbkweather.domain.model.DailyTemp
import com.mdrlzy.rbkweather.domain.model.DailyWeather
import com.mdrlzy.rbkweather.domain.model.HourlyWeather
import com.mdrlzy.rbkweather.domain.model.OneCallWeather
import com.mdrlzy.rbkweather.domain.model.WeatherCondition
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

data class LocalWeatherBundle(
    val cache: WeatherCacheEntity,
    val hourly: List<HourlyWeatherEntity>,
    val daily: List<DailyWeatherEntity>,
    val currentConditions: List<WeatherConditionEntity>,
)

fun OneCallResponseDto.toDomain(): OneCallWeather {
    return OneCallWeather(
        latitude = lat,
        longitude = lon,
        timezone = timezone,
        timezoneOffsetSeconds = timezoneOffset,
        current = current.toDomain(timezoneOffset),
        hourly = hourly.map { it.toDomain(timezoneOffset) },
        daily = daily.map { it.toDomain(timezoneOffset) },
    )
}

fun OneCallWeather.toLocalBundle(locationId: Long): LocalWeatherBundle {
    val currentEpoch = current.dateTime.toEpochSecond()

    val cache = WeatherCacheEntity(
        locationId = locationId,
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

    val hourlyEntities = hourly.map { item ->
        HourlyWeatherEntity(
            locationId = locationId,
            dateTimeEpochSeconds = item.dateTime.toEpochSecond(),
            temp = item.temp,
            precipitationProbability = item.precipitationProbability,
        )
    }

    val dailyEntities = daily.map { item ->
        DailyWeatherEntity(
            locationId = locationId,
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

    val currentConditions = current.weather.map { weather ->
        weather.toEntity(currentWeatherLocationId = locationId)
    }

    return LocalWeatherBundle(
        cache = cache,
        hourly = hourlyEntities,
        daily = dailyEntities,
        currentConditions = currentConditions,
    )
}

fun WeatherCacheEntity.toDomain(
    hourlyEntities: List<HourlyWeatherEntity>,
    dailyEntities: List<DailyWeatherEntity>,
    conditionEntities: List<WeatherConditionEntity>,
): OneCallWeather {
    val currentConditions = conditionEntities
        .filter { it.currentWeatherLocationId == this.locationId }
        .map { it.toDomain() }
    val conditionsByHourlyId = conditionEntities
        .filter { it.hourlyWeatherId != null }
        .groupBy { requireNotNull(it.hourlyWeatherId) }
    val conditionsByDailyId = conditionEntities
        .filter { it.dailyWeatherId != null }
        .groupBy { requireNotNull(it.dailyWeatherId) }
    val offset = timezoneOffsetSeconds

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
        hourly = hourlyEntities
            .sortedBy { it.dateTimeEpochSeconds }
            .map { item ->
            HourlyWeather(
                dateTime = item.dateTimeEpochSeconds.toOffsetDateTime(offset),
                temp = item.temp,
                precipitationProbability = item.precipitationProbability,
                weather = conditionsByHourlyId[item.id].orEmpty().map { it.toDomain() },
            )
        },
        daily = dailyEntities
            .sortedBy { it.dateTimeEpochSeconds }
            .map { item ->
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
                weather = conditionsByDailyId[item.id].orEmpty().map { it.toDomain() },
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
        feelsLike = feelsLike,
        pressure = pressure,
        humidity = humidity,
        uvIndex = uvi,
        windSpeed = windSpeed,
        windDeg = windDeg,
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
        windSpeed = windSpeed,
        windDeg = windDeg,
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

fun WeatherCondition.toEntity(
    currentWeatherLocationId: Long? = null,
    hourlyWeatherId: Long? = null,
    dailyWeatherId: Long? = null,
): WeatherConditionEntity {
    return WeatherConditionEntity(
        currentWeatherLocationId = currentWeatherLocationId,
        hourlyWeatherId = hourlyWeatherId,
        dailyWeatherId = dailyWeatherId,
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
