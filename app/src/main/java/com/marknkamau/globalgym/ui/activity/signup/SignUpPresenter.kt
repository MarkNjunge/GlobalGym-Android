package com.marknkamau.globalgym.ui.activity.signup

import com.marknkamau.globalgym.data.auth.AuthService
import io.reactivex.rxkotlin.subscribeBy

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class SignUpPresenter(private val view: SignUpView, private val authService: AuthService) {

    fun signUp(email: String, password: String) {
        authService.signUp(email, password)
                .andThen(authService.logIn(email, password))
                .subscribeBy(
                        onComplete = { view.onSignedUp() },
                        onError = {
                            view.displayMessage(it.message
                                    ?: "There was an error creating an account")
                        }
                )
    }

}