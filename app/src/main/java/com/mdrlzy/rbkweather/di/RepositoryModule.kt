package com.mdrlzy.rbkweather.di

import com.google.android.gms.location.LocationServices
import com.mdrlzy.rbkweather.data.local.WeatherLocalDataSource
import com.mdrlzy.rbkweather.data.local.WeatherLocalDataSourceImpl
import com.mdrlzy.rbkweather.data.remote.WeatherRemoteDataSource
import com.mdrlzy.rbkweather.data.remote.WeatherRemoteDataSourceImpl
import com.mdrlzy.rbkweather.data.repository.CityLocationRepositoryImpl
import com.mdrlzy.rbkweather.data.repository.LocationRepositoryImpl
import com.mdrlzy.rbkweather.data.repository.WeatherRepositoryImpl
import com.mdrlzy.rbkweather.domain.repository.CityLocationRepository
import com.mdrlzy.rbkweather.domain.repository.LocationRepository
import com.mdrlzy.rbkweather.domain.repository.WeatherRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single {
        LocationServices.getFusedLocationProviderClient(androidContext())
    }

    single<CityLocationRepository> { CityLocationRepositoryImpl(get(), get(), get(), get()) }
    single<LocationRepository> { LocationRepositoryImpl(androidContext(), get()) }
    single<WeatherRemoteDataSource> { WeatherRemoteDataSourceImpl(get()) }
    single<WeatherLocalDataSource> { WeatherLocalDataSourceImpl(get(), get(), get(), get(), get(), get(), get()) }
    single<WeatherRepository> { WeatherRepositoryImpl(get(), get()) }
}
