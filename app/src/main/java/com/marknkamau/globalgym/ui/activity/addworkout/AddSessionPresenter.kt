package com.marknkamau.globalgym.ui.activity.addworkout

import com.marknkamau.globalgym.data.local.PaperService
import com.marknkamau.globalgym.data.models.Exercise
import com.marknkamau.globalgym.data.models.Gym
import com.marknkamau.globalgym.data.models.Session
import com.marknkamau.globalgym.data.remote.ApiService
import com.marknkamau.globalgym.utils.RxUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class AddSessionPresenter(val view: AddSessionView, val paperService: PaperService, val apiService: ApiService) {

    private val disposables = CompositeDisposable()

    fun addSession(sessionName: String, dateTime: Long, gym: Gym, sessionSteps: List<Exercise>) {
        val session = Session(paperService.getUser()!!.userId, sessionName, dateTime, gym.gymId, sessionSteps)

        val disposable = apiService.createSession(session)
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

        disposables.add(disposable)
    }

    fun dispose(){
        disposables.dispose()
    }
}