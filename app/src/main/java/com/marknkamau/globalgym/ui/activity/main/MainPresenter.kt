package com.marknkamau.globalgym.ui.activity.main

import com.marknkamau.globalgym.data.auth.AuthService
import com.marknkamau.globalgym.data.local.PaperService
import com.marknkamau.globalgym.data.remote.ApiService
import com.marknkamau.globalgym.utils.NetworkUtils
import com.marknkamau.globalgym.utils.RxUtils
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
                    private val apiService: ApiService,
                    private val paperService: PaperService) {

    private val networkUtils = NetworkUtils()

    fun checkIfSignedIn() {
        if (!authService.isSignedIn()) {
            view.onNotSignedIn()
        } else {
            checkIfRegistered()
        }
    }

    private fun checkIfRegistered() {
        if (paperService.getUser() == null) {
            apiService.getUser(authService.getUser()!!.id)
                    .compose(RxUtils.applySingleSchedulers())
                    .subscribeBy(
                            onSuccess = { user ->
                                paperService.saveUser(user)
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
        } else {
            view.onSignedInAndRegistered()
        }
    }

}