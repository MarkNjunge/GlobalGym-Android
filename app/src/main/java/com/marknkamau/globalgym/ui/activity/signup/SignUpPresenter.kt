package com.marknkamau.globalgym.ui.activity.signup

import com.marknkamau.globalgym.data.auth.AuthService
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class SignUpPresenter(private val view: SignUpView, private val authService: AuthService) {

    private val compositeDisposable = CompositeDisposable()

    fun signUp(email: String, password: String) {
        val disposable = authService.signUp(email, password)
                .andThen(authService.logIn(email, password))
                .subscribeBy(
                        onComplete = { view.onSignedUp() },
                        onError = {
                            view.displayMessage(it.message
                                    ?: "There was an error creating an account")
                        }
                )

        compositeDisposable.add(disposable)
    }

    fun clearDisposables(){
        compositeDisposable.clear()
    }

}