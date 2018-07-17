package com.marknkamau.globalgym.ui.activity.main

import com.marknkamau.globalgym.data.auth.AuthService
import com.marknkamau.globalgym.data.repository.UserRepository
import com.marknkamau.globalgym.utils.NetworkUtils
import com.marknkamau.globalgym.utils.RxUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import retrofit2.HttpException
import timber.log.Timber

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class MainPresenter(private val view: MainView,
                    private val authService: AuthService,
                    private val userRepository: UserRepository) {

    private val networkUtils = NetworkUtils()
    private val compositeDisposable = CompositeDisposable()

    fun checkIfSignedIn() {
        if (!authService.isSignedIn()) {
            view.onNotSignedIn()
        } else {
            checkIfRegistered()
        }
    }

    private fun checkIfRegistered() {
        if (userRepository.getCurrentUser() == null) {
            val disposable = userRepository.setCurrentUser(authService.getUser()!!.id)
                    .compose(RxUtils.applySingleSchedulers())
                    .subscribeBy(
                            onSuccess = { user ->
                                Timber.d(user.toString())
                                view.onSignedInAndRegistered()
                            },
                            onError = {
                                if (it is HttpException) {
                                    val apiError = networkUtils.parseError(it.response())
                                    if (it.response().code() == 404) {
                                        view.onNotRegistered()
                                    }
                                } else {
                                    Timber.d(it)
                                }
                            }
                    )

            compositeDisposable.add(disposable)
        } else {
            view.onSignedInAndRegistered()
        }
    }

    fun clearDisposables() {
        compositeDisposable.clear()
    }

}