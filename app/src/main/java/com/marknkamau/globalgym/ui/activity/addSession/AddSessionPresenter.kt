package com.marknkamau.globalgym.ui.activity.addSession

import com.marknkamau.globalgym.data.models.Exercise
import com.marknkamau.globalgym.data.models.Gym
import com.marknkamau.globalgym.data.models.Session
import com.marknkamau.globalgym.data.repository.SessionsRepository
import com.marknkamau.globalgym.data.repository.UserRepository
import com.marknkamau.globalgym.utils.RxUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class AddSessionPresenter(private val view: AddSessionView,
                          private val userRepository: UserRepository,
                          private val sessionsRepository: SessionsRepository) {

    private val compositeDisposable = CompositeDisposable()

    fun addSession(sessionName: String, dateTime: Long, gym: Gym, sessionSteps: List<Exercise>) {
        userRepository.getCurrentUser()?.let { user ->
            val session = Session("", user.userId, sessionName, dateTime, gym.gymId, sessionSteps)

            val disposable = sessionsRepository.createSession(session)
                    .compose(RxUtils.applySingleSchedulers())
                    .subscribeBy(
                            onSuccess = {
                                view.onSessionAdded()
                            },
                            onError = {
                                Timber.e(it)
                                view.displayMessage(it.message ?: "Error adding session")
                            }
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