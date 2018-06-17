package com.marknkamau.globalgym.ui.login

import com.marknkamau.globalgym.data.auth.AuthService
import com.marknkamau.globalgym.data.remote.NetworkProvider
import com.marknkamau.globalgym.utils.RxUtils
import io.reactivex.rxkotlin.subscribeBy
import retrofit2.HttpException

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class LoginPresenter(private val view: LoginView, private val authService: AuthService, private val networkProvider: NetworkProvider) {

    fun sendPasswordReset(email: String) {
        authService.setPasswordReset(email)
    }

    fun logIn(email: String, password: String) {
        authService.logIn(email, password)
                .subscribeBy(
                        onComplete = { checkIfRegistered() },
                        onError = {
                            view.displayMessage(it.message ?: "There was an error logging in")
                        }
                )
    }

    private fun checkIfRegistered() {
        val id = authService.getUser()!!.id

        networkProvider.apiService.getUser(id)
                .compose(RxUtils.applySingleSchedulers())
                .subscribeBy(
                        onSuccess = {
                            view.onLoggedIn()
                        },
                        onError = {
                            if (it is HttpException) {
                                val apiError = networkProvider.parseError(it.response())
                                if (it.code() == 404) {
                                    view.onNotRegistered()
                                } else {
                                    view.displayMessage(apiError.message)
                                }
                            } else {
                                view.displayMessage(it.message ?: "There was an error logging in")
                            }
                        }
                )
    }

}