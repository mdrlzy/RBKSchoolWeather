package com.mdrlzy.rbkweather.di

import com.google.android.gms.location.LocationServices
import com.mdrlzy.rbkweather.data.repo.LocationRepoImpl
import com.mdrlzy.rbkweather.data.repo.WeatherRepoImpl
import com.mdrlzy.rbkweather.domain.repo.LocationRepo
import com.mdrlzy.rbkweather.domain.repo.WeatherRepo
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single {
        LocationServices.getFusedLocationProviderClient(androidContext())
    }

    single<LocationRepo> { LocationRepoImpl(get()) }
    single<WeatherRepo> { WeatherRepoImpl(get()) }
}
