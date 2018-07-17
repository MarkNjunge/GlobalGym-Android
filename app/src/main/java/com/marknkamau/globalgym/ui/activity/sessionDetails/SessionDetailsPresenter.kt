package com.marknkamau.globalgym.ui.activity.sessionDetails

import com.marknkamau.globalgym.data.models.SessionCompleted
import com.marknkamau.globalgym.data.repository.GymRepository
import com.marknkamau.globalgym.data.repository.SessionsRepository
import com.marknkamau.globalgym.utils.RxUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class SessionDetailsPresenter(private val view: SessionDetailsView,
                              private val gymRepository: GymRepository,
                              private val sessionsRepository: SessionsRepository) {

    private val compositeDisposable = CompositeDisposable()

    fun getGym(gymId: String) {
        val disposable = gymRepository.getGym(gymId)
                .compose(RxUtils.applySingleSchedulers())
                .subscribeBy(
                        onSuccess = { view.onGymRetrieved(it) },
                        onError = {
                            Timber.e(it)
                            view.displayMessage(it.message ?: "Error getting gym details")
                        }
                )

        compositeDisposable.add(disposable)
    }

    fun setSessionCompleted(sessionId: String) {
        val disposable = sessionsRepository.setSessionCompleted(SessionCompleted(sessionId))
                .compose(RxUtils.applyCompletableSchedulers())
                .subscribeBy(
                        onComplete = { view.onSessionCompleted() },
                        onError = {
                            Timber.e(it)
                            view.displayMessage(it.message ?: "Error setting session completed")
                        }
                )

        compositeDisposable.add(disposable)
    }

    fun deleteSession(sessionId: String) {
        val disposable = sessionsRepository.deleteSession(sessionId)
                .compose(RxUtils.applyCompletableSchedulers())
                .subscribeBy(
                        onComplete = {
                            view.onSessionDeleted()
                        },
                        onError = {
                            Timber.e(it)
                            view.displayMessage(it.message ?: "Error deleting session")
                        }
                )

        compositeDisposable.add(disposable)
    }

    fun clearDisposables() {
        compositeDisposable.clear()
    }

}