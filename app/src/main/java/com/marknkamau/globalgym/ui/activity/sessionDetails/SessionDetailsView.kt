package com.marknkamau.globalgym.ui.activity.sessionDetails

import com.marknkamau.globalgym.data.models.Gym

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

interface SessionDetailsView{
    fun displayMessage(message:String)
    fun onGymRetrieved(gym: Gym)
    fun onSessionCompleted()
    fun onSessionDeleted()
}