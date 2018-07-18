package com.marknkamau.globalgym.ui.activity.main

import com.marknkamau.globalgym.data.auth.AuthService

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class MainPresenter(private val view: MainView, private val authService: AuthService) {

    fun checkIfSignedIn() {
        if (!authService.isSignedIn()) {
            view.onNotSignedIn()
        }else{
            view.onSignedIn()
        }
    }

}