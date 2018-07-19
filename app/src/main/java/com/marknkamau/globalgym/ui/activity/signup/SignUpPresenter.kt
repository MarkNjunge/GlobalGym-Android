package com.marknkamau.globalgym.ui.activity.signup

import com.marknkamau.globalgym.data.auth.AuthService
import com.marknkamau.globalgym.utils.NetworkUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class SignUpPresenter(private val view: SignUpView, private val authService: AuthService) {

    private val compositeDisposable = CompositeDisposable()

    private val onError: (Throwable) -> Unit = { e ->
        Timber.e(e)
        when {
            e is UnknownHostException -> view.displayNoInternetMessage()
            e is HttpException -> {
                val apiError = NetworkUtils.parseError(e.response())
                view.displayMessage(apiError.message)
            }
            e.message != null -> view.displayMessage(e.message.toString())
            else -> view.displayDefaultErrorMessage()
        }
    }

    fun signUp(email: String, password: String) {
        val disposable = authService.signUp(email, password)
                .andThen(authService.logIn(email, password))
                .subscribeBy(
                        onComplete = { view.onSignedUp() },
                        onError = onError
                )

        compositeDisposable.add(disposable)
    }

    fun clearDisposables(){
        compositeDisposable.clear()
    }

}