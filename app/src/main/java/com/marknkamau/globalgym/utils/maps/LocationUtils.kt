package com.marknkamau.globalgym.utils.maps

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import com.google.android.gms.location.LocationServices
import io.reactivex.Single
import java.util.*

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

@SuppressLint("MissingPermission") // Location is required to reach this point
class LocationUtils(context: Context) {
    private val client = LocationServices.getFusedLocationProviderClient(context)
    private val geocoder = Geocoder(context, Locale.getDefault())

    fun getCurrentLocation(): Single<Location> {
        return Single.create { emitter ->
            client.lastLocation
                    .addOnSuccessListener { location ->
                        emitter.onSuccess(location)
                    }
                    .addOnFailureListener { exception ->
                        emitter.onError(exception)
                    }
        }
    }

    fun reverseGeocode(lat: Double, lng: Double): Single<Address> {
        return Single.create { emitter ->
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            if (addresses.isEmpty()) {
                emitter.onError(Throwable("Unable to get location"))
            } else {
                emitter.onSuccess(addresses.first())
            }
        }
    }

}
