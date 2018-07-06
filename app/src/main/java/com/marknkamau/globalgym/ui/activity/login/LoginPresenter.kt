package com.marknkamau.globalgym.ui.activity.login

import com.marknkamau.globalgym.data.auth.AuthService
import com.marknkamau.globalgym.data.remote.ApiService
import com.marknkamau.globalgym.data.repository.DataRepository
import com.marknkamau.globalgym.utils.NetworkUtils
import com.marknkamau.globalgym.utils.RxUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import retrofit2.HttpException

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class LoginPresenter(private val view: LoginView,
                     private val authService: AuthService,
                     private val dataRepository: DataRepository) {

    private val networkUtils = NetworkUtils()
    private val compositeDisposable = CompositeDisposable()

    fun sendPasswordReset(email: String) {
        authService.setPasswordReset(email)
    }

    fun logIn(email: String, password: String) {
        val disposable = authService.logIn(email, password)
                .subscribeBy(
                        onComplete = { checkIfRegistered() },
                        onError = {
                            view.displayMessage(it.message ?: "There was an error logging in")
                        }
                )

        compositeDisposable.add(disposable)
    }

    private fun checkIfRegistered() {
        val id = authService.getUser()!!.id

        val disposable = dataRepository.apiService.getUser(id)
                .compose(RxUtils.applySingleSchedulers())
                .subscribeBy(
                        onSuccess = {
                            view.onLoggedIn()
                        },
                        onError = {
                            if (it is HttpException) {
                                val apiError = networkUtils.parseError(it.response())
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

        compositeDisposable.add(disposable)
    }

    fun clearDisposables(){
        compositeDisposable.clear()
    }

}