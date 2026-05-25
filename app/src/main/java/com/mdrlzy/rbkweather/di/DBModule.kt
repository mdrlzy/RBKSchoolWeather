package com.mdrlzy.rbkweather.di

import androidx.room.Room
import com.mdrlzy.rbkweather.data.local.WeatherDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val WEATHER_DATABASE_NAME = "weather.db"

val dbModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            WeatherDatabase::class.java,
            WEATHER_DATABASE_NAME,
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }

    single { get<WeatherDatabase>().weatherCacheDao() }
    single { get<WeatherDatabase>().hourlyWeatherDao() }
    single { get<WeatherDatabase>().dailyWeatherDao() }
    single { get<WeatherDatabase>().weatherConditionDao() }
    single { get<WeatherDatabase>().cityLocationDao() }
}
