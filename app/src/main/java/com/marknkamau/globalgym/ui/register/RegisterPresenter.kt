package com.marknkamau.globalgym.ui.register

import com.marknkamau.globalgym.data.auth.AuthService
import com.marknkamau.globalgym.data.models.User
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

class RegisterPresenter(private val view: RegisterView,
                        private val authService: AuthService,
                        private val networkProvider: NetworkProvider) {

    fun registerUser(firstName: String,
                     lastName: String,
                     phone: String,
                     year: Int,
                     country: String,
                     gender: String,
                     weight: Int,
                     targetWeight: Int) {

        if (!authService.isSignedIn())
            return

        val user = User(authService.getUser()!!.id,
                firstName,
                lastName,
                authService.getUser()!!.email,
                phone,
                "http://via.placeholder.com/350x350",
                year,
                country,
                gender.first().toString(),
                weight,
                targetWeight)

        networkProvider.apiService.registerUser(user)
                .compose(RxUtils.applySingleSchedulers())
                .subscribeBy(
                        onSuccess = {
                            view.onRegistered()
                        },
                        onError = {
                            if (it is HttpException) {
                                val apiError = networkProvider.parseError(it.response())
                                Timber.d(apiError.message)
                                view.displayMessage(apiError.message)
                            } else {
                                Timber.e(it)
                                view.displayMessage(it.message ?: "Unable to create account")
                            }
                        }
                )
    }
}