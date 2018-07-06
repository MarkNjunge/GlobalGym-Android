package com.marknkamau.globalgym.ui.activity.addSession

import com.marknkamau.globalgym.data.local.PaperService
import com.marknkamau.globalgym.data.models.Exercise
import com.marknkamau.globalgym.data.models.Gym
import com.marknkamau.globalgym.data.models.Session
import com.marknkamau.globalgym.data.remote.ApiService
import com.marknkamau.globalgym.data.repository.DataRepository
import com.marknkamau.globalgym.utils.RxUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class AddSessionPresenter(val view: AddSessionView, val dataRepository: DataRepository) {

    private val compositeDisposable = CompositeDisposable()

    fun addSession(sessionName: String, dateTime: Long, gym: Gym, sessionSteps: List<Exercise>) {
        val session = Session("", dataRepository.paperService.getUser()!!.userId, sessionName, dateTime, gym.gymId, sessionSteps)

        val disposable = dataRepository.apiService.createSession(session)
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
    }

    fun clearDisposables() {
        compositeDisposable.clear()
    }
}