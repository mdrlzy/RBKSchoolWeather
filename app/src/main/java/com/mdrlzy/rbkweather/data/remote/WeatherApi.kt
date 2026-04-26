package com.mdrlzy.rbkweather.data.remote

import com.mdrlzy.rbkweather.data.remote.dto.OneCallResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/3.0/onecall")
    suspend fun getOneCall(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "ru",
    ): OneCallResponseDto
}