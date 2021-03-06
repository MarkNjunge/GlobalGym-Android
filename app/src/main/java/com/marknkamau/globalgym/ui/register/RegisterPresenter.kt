package com.marknkamau.globalgym.ui.register

import com.marknkamau.globalgym.data.auth.AuthService
import com.marknkamau.globalgym.data.models.User
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

class RegisterPresenter(private val view: RegisterView,
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

        val profileImage = if (gender == "Male") {
            "https://firebasestorage.googleapis.com/v0/b/globalgym-65a9a.appspot.com/o/profile_male.jpg?alt=media&token=dfb93938-171a-4cf7-8c72-177f09821805"
        } else {
            "https://firebasestorage.googleapis.com/v0/b/globalgym-65a9a.appspot.com/o/profile_female.jpg?alt=media&token=89a66237-e4ea-45d8-b56b-2994319b7b76"
        }

        val user = User(authService.getUser()!!.id,
                firstName,
                lastName,
                authService.getUser()!!.email,
                phone,
                profileImage,
                year,
                country,
                gender.first().toString(),
                weight,
                targetWeight,
                null)

        val disposable = userRepository.registerUser(user)
                .compose(RxUtils.applySingleSchedulers())
                .subscribeBy(
                        onSuccess = {
                            view.onRegistered()
                        },
                        onError = onError
                )

        compositeDisposable.add(disposable)
    }

    fun clearDisposables(){
        compositeDisposable.clear()
    }
}