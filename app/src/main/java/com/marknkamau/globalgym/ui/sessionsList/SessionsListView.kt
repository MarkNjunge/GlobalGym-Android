package com.marknkamau.globalgym.ui.sessionsList

import com.marknkamau.globalgym.data.models.Session
import com.marknkamau.globalgym.ui.base.BaseView

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