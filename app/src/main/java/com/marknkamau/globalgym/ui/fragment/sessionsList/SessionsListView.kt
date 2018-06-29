package com.marknkamau.globalgym.ui.fragment.sessionsList

import com.marknkamau.globalgym.data.models.Session

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

interface SessionsListView {
    fun displayMessage(message: String)
    fun hideLoading()
    fun showLoading()
    fun onSessionsRetrieved(sessions: List<Session>)
}