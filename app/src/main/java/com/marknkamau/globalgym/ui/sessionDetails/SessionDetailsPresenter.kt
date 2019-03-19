package com.marknkamau.globalgym.ui.sessionDetails

import com.marknkamau.globalgym.data.models.SessionCompleted
import com.marknkamau.globalgym.data.repository.GymRepository
import com.marknkamau.globalgym.data.repository.SessionsRepository
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

class SessionDetailsPresenter(private val view: SessionDetailsView,
                              private val gymRepository: GymRepository,
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

    fun getGym(gymId: String) {
        val disposable = gymRepository.getGym(gymId)
                .compose(RxUtils.applySingleSchedulers())
                .subscribeBy(
                        onSuccess = { view.onGymRetrieved(it) },
                        onError = onError
                )

        compositeDisposable.add(disposable)
    }

    fun setSessionCompleted(sessionId: String) {
        val disposable = sessionsRepository.setSessionCompleted(SessionCompleted(sessionId))
                .compose(RxUtils.applyCompletableSchedulers())
                .subscribeBy(
                        onComplete = { view.onSessionCompleted() },
                        onError = onError
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
                        onError = onError
                )

        compositeDisposable.add(disposable)
    }

    fun clearDisposables() {
        compositeDisposable.clear()
    }

}