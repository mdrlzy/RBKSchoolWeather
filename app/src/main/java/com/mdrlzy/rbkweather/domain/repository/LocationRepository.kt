package com.mdrlzy.rbkweather.domain.repository

import com.mdrlzy.rbkweather.domain.model.LocationData

interface LocationRepository {
    suspend fun getCurrentLocation(): LocationData?
}