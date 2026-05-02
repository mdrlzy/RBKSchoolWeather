package com.mdrlzy.rbkweather.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mdrlzy.rbkweather.data.local.dao.CityLocationDao
import com.mdrlzy.rbkweather.data.local.dao.DailyWeatherDao
import com.mdrlzy.rbkweather.data.local.dao.HourlyWeatherDao
import com.mdrlzy.rbkweather.data.local.dao.WeatherCacheDao
import com.mdrlzy.rbkweather.data.local.dao.WeatherConditionDao
import com.mdrlzy.rbkweather.data.local.entity.CityLocationEntity
import com.mdrlzy.rbkweather.data.local.entity.DailyWeatherEntity
import com.mdrlzy.rbkweather.data.local.entity.HourlyWeatherEntity
import com.mdrlzy.rbkweather.data.local.entity.WeatherCacheEntity
import com.mdrlzy.rbkweather.data.local.entity.WeatherConditionEntity

@Database(
    entities = [
        WeatherCacheEntity::class,
        HourlyWeatherEntity::class,
        DailyWeatherEntity::class,
        WeatherConditionEntity::class,
        CityLocationEntity::class,
    ],
    version = 3,
    exportSchema = true,
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherCacheDao(): WeatherCacheDao
    abstract fun hourlyWeatherDao(): HourlyWeatherDao
    abstract fun dailyWeatherDao(): DailyWeatherDao
    abstract fun weatherConditionDao(): WeatherConditionDao
    abstract fun cityLocationDao(): CityLocationDao
}
