package com.mdrlzy.rbkweather.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import com.google.android.gms.location.FusedLocationProviderClient
import com.mdrlzy.rbkweather.domain.model.LocationData
import com.mdrlzy.rbkweather.domain.repository.LocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume

class LocationRepositoryImpl(
    private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient,
): LocationRepository {

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

    override suspend fun getCityName(locationData: LocationData): String? {
        if (!Geocoder.isPresent()) return null

        val geocoder = Geocoder(context, Locale.getDefault())

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            suspendCancellableCoroutine { continuation ->
                geocoder.getFromLocation(
                    locationData.latitude,
                    locationData.longitude,
                    1,
                    object : Geocoder.GeocodeListener {
                        override fun onGeocode(addresses: MutableList<Address>) {
                            continuation.resume(addresses.firstOrNull()?.cityName)
                        }

                        override fun onError(errorMessage: String?) {
                            continuation.resume(null)
                        }
                    },
                )
            }
        } else {
            withContext(Dispatchers.IO) {
                runCatching {
                    @Suppress("DEPRECATION")
                    geocoder.getFromLocation(
                        locationData.latitude,
                        locationData.longitude,
                        1,
                    )
                        ?.firstOrNull()
                        ?.cityName
                }.getOrNull()
            }
        }
    }
}

private val Address.cityName: String?
    get() = locality ?: subAdminArea ?: adminArea