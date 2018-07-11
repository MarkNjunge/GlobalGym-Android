package com.marknkamau.globalgym.utils.maps

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.marknkamau.globalgym.data.models.Gym
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.marknkamau.globalgym.R
import android.support.v4.content.ContextCompat
import com.marknkamau.globalgym.utils.toBitmap


/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class MapUtils(private val context: Context, private val googleMap: GoogleMap) {

    fun addGymMarker(gym: Gym, preferred: Boolean = false) {
        val drawable = if (preferred) R.drawable.ic_gym_pin_preferred else R.drawable.ic_gym_pin
        val bitmap = ContextCompat.getDrawable(context, drawable)!!.toBitmap()
        val icon = BitmapDescriptorFactory.fromBitmap(bitmap)
        val gymLoc = LatLng(gym.cords.lat, gym.cords.lng)
        val markerOptions = MarkerOptions()
                .position(gymLoc)
                .title(gym.name)
                .icon(icon)

        val marker = googleMap.addMarker(markerOptions)
        marker.tag = gym
    }

}