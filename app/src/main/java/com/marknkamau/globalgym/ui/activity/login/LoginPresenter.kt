package com.marknkamau.globalgym.ui.activity.login

import com.marknkamau.globalgym.data.auth.AuthService
import com.marknkamau.globalgym.data.repository.UserRepository
import com.marknkamau.globalgym.utils.NetworkUtils
import com.marknkamau.globalgym.utils.RxUtils
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

class LoginPresenter(private val view: LoginView,
                     private val authService: AuthService,
                     private val userRepository: UserRepository) {

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

    fun sendPasswordReset(email: String) {
        authService.setPasswordReset(email)
                .compose(RxUtils.applyCompletableSchedulers())
                .subscribeBy(
                        onComplete = {
                            view.displayMessage("Password reset email sent")
                        },
                        onError = onError
                )
    }

    fun logIn(email: String, password: String) {
        val disposable = authService.logIn(email, password)
                .subscribeBy(
                        onComplete = { checkIfRegistered() },
                        onError = onError
                )

        compositeDisposable.add(disposable)
    }

    private fun checkIfRegistered() {
        val id = authService.getUser()!!.id
        val disposable = userRepository.getUser(id)
                .compose(RxUtils.applySingleSchedulers())
                .flatMapCompletable { user ->
                    userRepository.setCurrentUser(user)
                }
                .compose(RxUtils.applyCompletableSchedulers())
                .subscribeBy(
                        onComplete = {
                            view.onLoggedIn()
                        },
                        onError = {
                            if (it is HttpException && it.code() == 404) {
                                view.onNotRegistered()
                            } else {
                                onError(it)
                            }
                        }
                )

        compositeDisposable.add(disposable)
    }

    fun clearDisposables() {
        compositeDisposable.clear()
    }

}