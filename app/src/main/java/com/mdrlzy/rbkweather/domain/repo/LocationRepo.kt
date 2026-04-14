package com.mdrlzy.rbkweather.domain.repo

import com.mdrlzy.rbkweather.domain.model.LocationData

interface LocationRepo {
    suspend fun getCurrentLocation(): LocationData?
}