package com.mdrlzy.rbkweather.domain.repository

import kotlinx.coroutines.flow.Flow

interface Preferences {
    val isCelciusNotFarenheit: Flow<Boolean>

    suspend fun setIsCelciusNotFarenheit(value: Boolean)
}
