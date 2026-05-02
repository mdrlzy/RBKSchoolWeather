package com.mdrlzy.rbkweather.di

import com.mdrlzy.rbkweather.presentation.citylist.CityListViewModel
import com.mdrlzy.rbkweather.presentation.home.HomeViewModel
import com.mdrlzy.rbkweather.presentation.location.LocationPermissionHelper
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    single { LocationPermissionHelper(androidContext()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { CityListViewModel(get(), get()) }
}
