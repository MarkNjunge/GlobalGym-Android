package com.marknkamau.globalgym.ui.fragment.sessionsList

import com.marknkamau.globalgym.data.repository.SessionsRepository
import com.marknkamau.globalgym.utils.RxUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class SessionsListPresenter(private val view: SessionsListView, private val sessionRepository: SessionsRepository) {

    private val compositeDisposable = CompositeDisposable()

    fun getSessions() {
        view.showLoading()
        val disposable = sessionRepository.getSessions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread(), true)
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