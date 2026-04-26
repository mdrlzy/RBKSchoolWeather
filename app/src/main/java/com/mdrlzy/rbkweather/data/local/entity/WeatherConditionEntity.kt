package com.mdrlzy.rbkweather.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeatherConditionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ownerType: String,
    val ownerIndex: Int,
    val position: Int,
    val weatherId: Int,
    val main: String,
    val description: String,
    val icon: String,
)
