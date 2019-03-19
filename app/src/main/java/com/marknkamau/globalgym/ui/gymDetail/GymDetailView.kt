package com.marknkamau.globalgym.ui.gymDetail

import com.marknkamau.globalgym.data.models.Instructor
import com.marknkamau.globalgym.ui.base.BaseView

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

interface GymDetailView : BaseView {
    fun onInstructorsReceived(instructors: List<Instructor>)
    fun onGymIsPreferred()
}