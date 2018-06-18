package com.marknkamau.globalgym.ui.main

import com.marknkamau.globalgym.data.auth.AuthService
import com.marknkamau.globalgym.data.local.PaperService
import com.marknkamau.globalgym.data.remote.NetworkProvider
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
                    private val networkProvider: NetworkProvider,
                    private val paperService: PaperService) {

    fun checkIfSignedIn() {
        if (!authService.isSignedIn()) {
            view.onNotSignedIn()
        } else {
            checkIfRegistered()
        }
    }

    private fun checkIfRegistered() {
        if (paperService.getUser() == null) {
            networkProvider.apiService.getUser(authService.getUser()!!.id)
                    .compose(RxUtils.applySingleSchedulers())
                    .subscribeBy(
                            onSuccess = { user ->
                                paperService.saveUser(user)
                                Timber.d(user.toString())
                            },
                            onError = {
                                if (it is HttpException) {
                                    val apiError = networkProvider.parseError(it.response())
                                    if (it.response().code() == 404) {
                                        view.onNotRegistered()
                                    }
                                } else {
                                    Timber.d(it)
                                }
                            }
                    )
        }
    }

}