package com.mdrlzy.rbkweather.data.repo

import com.mdrlzy.rbkweather.BuildConfig
import com.mdrlzy.rbkweather.data.network.WeatherApi
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
import com.mdrlzy.rbkweather.domain.model.LocationData
import com.mdrlzy.rbkweather.domain.model.OneCallWeather
import com.mdrlzy.rbkweather.domain.model.WeatherCondition
import com.mdrlzy.rbkweather.domain.repo.WeatherRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

class WeatherRepoImpl(private val api: WeatherApi): WeatherRepo {
    override suspend fun getCurrent(
        locationData: LocationData
    ): Result<OneCallWeather> = withContext(Dispatchers.IO) {
        runCatching {
            api.getOneCall(
                lat = locationData.latitude,
                lon = locationData.longitude,
                apiKey = BuildConfig.WEATHER_API_KEY,
            ).toDomain()
        }
    }
}

private fun OneCallResponseDto.toDomain(): OneCallWeather {
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

private fun Long.toOffsetDateTime(timezoneOffsetSeconds: Int): OffsetDateTime {
    return OffsetDateTime.ofInstant(
        Instant.ofEpochSecond(this),
        ZoneOffset.ofTotalSeconds(timezoneOffsetSeconds),
    )
}