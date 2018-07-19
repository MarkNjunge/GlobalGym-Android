package com.marknkamau.globalgym.ui.fragment.sessionsList

import com.marknkamau.globalgym.data.models.Session
import com.marknkamau.globalgym.ui.BaseView

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

interface SessionsListView : BaseView {
    fun hideLoading()
    fun showLoading()
    fun onSessionsRetrieved(sessions: List<Session>)
}