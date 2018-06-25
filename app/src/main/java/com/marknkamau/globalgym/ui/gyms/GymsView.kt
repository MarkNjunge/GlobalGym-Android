package com.marknkamau.globalgym.ui.gyms

import com.marknkamau.globalgym.data.models.Gym

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

interface GymsView {
    fun displayMessage(message: String)
    fun onGymsRetrieved(gyms: List<Gym>)
}