package com.marknkamau.globalgym.ui.fragment.sessionsList

import com.marknkamau.globalgym.data.local.PaperService
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

class SessionsListPresenter(val view: SessionsListView, val paperService: PaperService, val apiService: ApiService) {

    private val compositeDisposable = CompositeDisposable()

    fun getSessions() {
        view.showLoading()
        val disposable = apiService.getSessions(paperService.getUser()!!.userId)
                .compose(RxUtils.applySingleSchedulers())
                .subscribeBy(
                        onSuccess = {
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