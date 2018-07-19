package com.marknkamau.globalgym.ui.activity.addSession

import com.marknkamau.globalgym.data.models.Exercise
import com.marknkamau.globalgym.data.models.Gym
import com.marknkamau.globalgym.data.models.Session
import com.marknkamau.globalgym.data.repository.SessionsRepository
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

class AddSessionPresenter(private val view: AddSessionView,
                          private val userRepository: UserRepository,
                          private val sessionsRepository: SessionsRepository) {

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

    fun addSession(sessionName: String, dateTime: Long, gym: Gym, sessionSteps: List<Exercise>) {
        userRepository.getCurrentUser()?.let { user ->
            val session = Session("", user.userId, sessionName, dateTime, gym.gymId, sessionSteps)

            val disposable = sessionsRepository.createSession(session)
                    .compose(RxUtils.applySingleSchedulers())
                    .subscribeBy(
                            onSuccess = {
                                view.onSessionAdded()
                            },
                            onError = onError
                    )

            compositeDisposable.add(disposable)
            return
        }

        Timber.e("User is not signed in")
        view.displayMessage("Please sign in again")
    }

    fun clearDisposables() {
        compositeDisposable.clear()
    }
}