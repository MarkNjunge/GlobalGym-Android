package com.marknkamau.globalgym.ui.activity.sessionDetails

import com.marknkamau.globalgym.data.models.Gym
import com.marknkamau.globalgym.ui.BaseView

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

interface SessionDetailsView : BaseView{
    fun onGymRetrieved(gym: Gym)
    fun onSessionCompleted()
    fun onSessionDeleted()
}