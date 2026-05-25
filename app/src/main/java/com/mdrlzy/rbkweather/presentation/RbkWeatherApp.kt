package com.mdrlzy.rbkweather.presentation

import android.app.Application
import com.mdrlzy.rbkweather.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RbkWeatherApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@RbkWeatherApp)
            modules(appModule)
        }
    }
}