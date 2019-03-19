package com.marknkamau.globalgym.ui.profile

import com.marknkamau.globalgym.data.models.Gym
import com.marknkamau.globalgym.data.models.User
import com.marknkamau.globalgym.ui.base.BaseView

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

interface ProfileView : BaseView {
    fun onUserRetrieved(user: User)
    fun onGymRetrieved(gym: Gym)
}