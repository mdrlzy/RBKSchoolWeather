package com.mdrlzy.rbkweather.data.repo

import com.mdrlzy.rbkweather.BuildConfig
import com.mdrlzy.rbkweather.data.network.WeatherApi
import com.mdrlzy.rbkweather.data.network.dto.WeatherResponse
import com.mdrlzy.rbkweather.domain.model.LocationData
import com.mdrlzy.rbkweather.domain.model.Weather
import com.mdrlzy.rbkweather.domain.repo.WeatherRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepoImpl(private val api: WeatherApi): WeatherRepo {
    override suspend fun getCurrent(
        locationData: LocationData
    ): Result<Weather> = withContext(Dispatchers.IO) {
        runCatching {
            api.getWeather(
                lat = locationData.latitude,
                lon = locationData.longitude,
                apiKey = BuildConfig.WEATHER_API_KEY,
            ).toDomain()
        }
    }
}

private fun WeatherResponse.toDomain(): Weather {
    val weather = weather.firstOrNull()

    return Weather(
        temperature = main.temp.toInt(),
        feelsLike = main.feels_like.toInt(),
        description = weather?.description.orEmpty(),
        windSpeed = wind.speed,
        cityName = name,
    )
}