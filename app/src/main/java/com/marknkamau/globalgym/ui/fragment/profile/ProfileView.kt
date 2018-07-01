package com.marknkamau.globalgym.ui.fragment.profile

import com.marknkamau.globalgym.data.models.Gym
import com.marknkamau.globalgym.data.models.User

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

interface ProfileView {
    fun displayMessage(message: String)
    fun onUserRetrieved(user: User)
    fun onGymRetrieved(gym: Gym)
}