package com.marknkamau.globalgym.ui.profile

import com.marknkamau.globalgym.data.models.User

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

interface ProfileView {
    fun displayMessage(message: String)
    fun onUserRetrieved(user: User)
}