package com.marknkamau.globalgym.utils.maps

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.marknkamau.globalgym.data.models.Gym

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class MapUtils(private val googleMap: GoogleMap) {

    fun addGymMarker(gym: Gym) {
        val gymLoc = LatLng(gym.cords.lat, gym.cords.lng)
        val markerOptions = MarkerOptions()
                .position(gymLoc)
                .title(gym.name)

        val marker = googleMap.addMarker(markerOptions)
        marker.tag = gym

        googleMap.addMarker(markerOptions)
    }

}