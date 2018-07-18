package com.marknkamau.globalgym.ui.activity.main

import com.marknkamau.globalgym.data.auth.AuthService
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class MainPresenter(private val view: MainView,
                    private val authService: AuthService) {

    private val compositeDisposable = CompositeDisposable()

    fun checkIfSignedIn() {
        if (!authService.isSignedIn()) {
            view.onNotSignedIn()
        }
    }

    fun clearDisposables() {
        compositeDisposable.clear()
    }

}