package com.marknkamau.globalgym.ui.gyms

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.marknkamau.globalgym.R
import com.marknkamau.globalgym.data.models.Gym
import com.marknkamau.globalgym.utils.GlideApp
import com.mikhaellopez.circularimageview.CircularImageView

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class GymInfoWindowAdapter(private val context: Context) : GoogleMap.InfoWindowAdapter {
    override fun getInfoContents(marker: Marker): View {
        val view = (context as Activity).layoutInflater.inflate(R.layout.layout_info_window, null)

        val imgGymIcon = view.findViewById<CircularImageView>(R.id.imgGymIcon)
        val tvTitle = view.findViewById<TextView>(R.id.tvGymTitle)
        val tvTime = view.findViewById<TextView>(R.id.tvGymTime)

        val gym = marker.tag as Gym

        GlideApp.with(context)
                .asBitmap()
                .load(gym.logo)
                .dontAnimate()
                .circleCrop()
                .into(imgGymIcon)

        tvTitle.text = gym.name
        tvTime.text = "${gym.openTime} - ${gym.closeTime}"

        return view
    }

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }
}