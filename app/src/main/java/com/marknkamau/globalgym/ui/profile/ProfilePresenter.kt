package com.marknkamau.globalgym.ui.profile

import com.marknkamau.globalgym.data.auth.AuthService
import com.marknkamau.globalgym.data.local.PaperService
import com.marknkamau.globalgym.data.remote.ApiService
import com.marknkamau.globalgym.utils.RxUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class ProfilePresenter(private val view: ProfileView,
                       private val authService: AuthService,
                       private val paperService: PaperService,
                       private val apiService: ApiService) {


    private val compositeDisposable = CompositeDisposable()

    fun dispose() {
        compositeDisposable.dispose()
    }

    fun getUser() {
        paperService.getUser()?.let {
            view.onUserRetrieved(it)
        }

        val disposable = apiService.getUser(authService.getUser()!!.id)
                .compose(RxUtils.applySingleSchedulers())
                .subscribeBy(
                        onSuccess = { user ->
                            view.onUserRetrieved(user)
                            paperService.saveUser(user)
                        },
                        onError = {
                            view.displayMessage(it.message
                                    ?: "There was an error retrieving your profile")
                        }
                )

        compositeDisposable.add(disposable)
    }

}