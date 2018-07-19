package com.marknkamau.globalgym.ui.fragment.gyms

import com.marknkamau.globalgym.data.models.Gym
import com.marknkamau.globalgym.ui.BaseView

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

interface GymsView : BaseView {
    fun onGymsRetrieved(gyms: List<Gym>)
}