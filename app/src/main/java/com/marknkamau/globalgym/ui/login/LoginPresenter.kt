package com.marknkamau.globalgym.ui.login

import com.marknkamau.globalgym.data.auth.AuthService
import io.reactivex.rxkotlin.subscribeBy

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class LoginPresenter(private val view: LoginView, private val authService: AuthService) {

    fun sendPasswordReset(email: String){
        authService.setPasswordReset(email)
    }

    fun logIn(email: String, password: String) {
        authService.logIn(email, password)
                .subscribeBy(
                        onComplete = { view.onLoggedIn() },
                        onError = {
                            view.displayMessage(it.message ?: "There was an error logging in")
                        }
                )
    }

}