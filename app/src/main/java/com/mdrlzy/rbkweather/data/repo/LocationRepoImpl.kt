package com.mdrlzy.rbkweather.data.repo

import android.annotation.SuppressLint
import com.google.android.gms.location.FusedLocationProviderClient
import com.mdrlzy.rbkweather.domain.model.LocationData
import com.mdrlzy.rbkweather.domain.repo.LocationRepo
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class LocationRepoImpl(
    private val fusedLocationClient: FusedLocationProviderClient
): LocationRepo {

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): LocationData? =
        suspendCancellableCoroutine { continuation ->

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    val result = location?.let {
                        LocationData(
                            latitude = it.latitude,
                            longitude = it.longitude
                        )
                    }
                    continuation.resume(result)
                }
                .addOnFailureListener {
                    continuation.resume(null)
                }
        }
}