package com.marknkamau.globalgym.ui.activity.login

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

interface LoginView {
    fun displayMessage(message: String)
    fun onLoggedIn()
    fun onNotRegistered()
}