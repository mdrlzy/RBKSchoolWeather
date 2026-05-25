package com.mdrlzy.rbkweather.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CityLocationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String?,
    val latitude: Double,
    val longitude: Double,
)
