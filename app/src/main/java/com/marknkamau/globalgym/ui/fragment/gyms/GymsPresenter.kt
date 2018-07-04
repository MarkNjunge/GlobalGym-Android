package com.marknkamau.globalgym.ui.fragment.gyms

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

class GymsPresenter(private val view: GymsView, private val apiService: ApiService) {

    private val compositeDisposable = CompositeDisposable()

    fun getGyms(lat: Double, lng: Double, country: String) {
        val disposable = apiService.getNearbyGyms(country, lat, lng, 50 * 1000)
                .compose(RxUtils.applySingleSchedulers())
                .subscribeBy(
                        onSuccess = { gyms ->
                            if (gyms.isEmpty()) {
                                view.displayMessage("There are no gyms nearby")
                            } else {
                                view.onGymsRetrieved(gyms)
                            }
                        },
                        onError = {
                            Timber.d(it)
                            view.displayMessage(it.message ?: "Error finding gyms")
                        }

                )

        compositeDisposable.add(disposable)
    }

    fun searchForGym(name: String) {
        view.showSearchLoading()
        val disposable = apiService.searchGyms(name)
                .compose(RxUtils.applySingleSchedulers())
                .subscribeBy(
                        onSuccess = { gyms ->
                            view.hideSearchLoading()
                            if (gyms.isEmpty()) {
                                view.displayMessage("No gyms have been found")
                            } else {
                                view.onGymSearchResultRetrieved(gyms)
                            }
                        },
                        onError = {
                            view.hideSearchLoading()
                            Timber.d(it)
                            view.displayMessage(it.message ?: "Error finding gyms")
                        }
                )

        compositeDisposable.add(disposable)
    }

    fun clearDisposables() {
        compositeDisposable.clear()
    }
}