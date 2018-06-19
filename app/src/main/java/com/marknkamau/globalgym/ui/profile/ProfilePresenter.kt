package com.marknkamau.globalgym.ui.profile

import com.marknkamau.globalgym.data.auth.AuthService
import com.marknkamau.globalgym.data.local.PaperService
import com.marknkamau.globalgym.data.remote.NetworkProvider
import com.marknkamau.globalgym.utils.RxUtils
import io.reactivex.rxkotlin.subscribeBy

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class ProfilePresenter(private val view: ProfileView,
                       private val authService: AuthService,
                       private val paperService: PaperService,
                       private val networkProvider: NetworkProvider) {

    fun getUser() {
        paperService.getUser()?.let {
            view.onUserRetrieved(it)
        }

        networkProvider.apiService.getUser(authService.getUser()!!.id)
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
    }

}