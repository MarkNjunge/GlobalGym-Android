package com.marknkamau.globalgym.ui.gymdetail

import com.marknkamau.globalgym.data.models.Instructor

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

interface GymDetailView {
    fun displayMessage(message: String)
    fun onInstructorsReceived(instructors: List<Instructor>)
}