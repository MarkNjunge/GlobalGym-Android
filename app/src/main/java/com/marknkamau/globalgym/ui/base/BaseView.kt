package com.marknkamau.globalgym.ui.base

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

interface BaseView{
    fun displayNoInternetMessage()

    fun displayDefaultErrorMessage()

    fun displayMessage(message: String)
}