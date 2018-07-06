package com.marknkamau.globalgym.ui.fragment.sessionsList

import com.marknkamau.globalgym.data.repository.DataRepository
import com.marknkamau.globalgym.utils.RxUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class SessionsListPresenter(val view: SessionsListView, val repository: DataRepository) {

    private val compositeDisposable = CompositeDisposable()

    fun getSessions() {
        view.showLoading()
        val disposable = repository.getSessions()
                .debounce(400, TimeUnit.MILLISECONDS)
                .compose(RxUtils.applyObservableSchedulers())
                .subscribeBy(
                        onNext = {
                            view.hideLoading()
                            view.onSessionsRetrieved(it)
                        },
                        onError = {
                            Timber.e(it)
                            view.hideLoading()
                            view.displayMessage(it.message ?: "Error retrieving sessions")
                        }
                )

        compositeDisposable.add(disposable)
    }

    fun clearDisposables() {
        compositeDisposable.clear()
    }

}