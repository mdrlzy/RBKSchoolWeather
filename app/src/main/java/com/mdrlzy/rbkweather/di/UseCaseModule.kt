package com.mdrlzy.rbkweather.di

import com.mdrlzy.rbkweather.domain.usecase.GetCurrentWeatherUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetCurrentWeatherUseCase(get(), get()) }
}
