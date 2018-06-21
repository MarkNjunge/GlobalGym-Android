package com.marknkamau.globalgym.ui.gyms

import com.marknkamau.globalgym.data.remote.ApiService
import com.marknkamau.globalgym.data.remote.NetworkProvider
import com.marknkamau.globalgym.utils.RxUtils
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class GymsPresenter(private val view: GymsView, private val apiService: ApiService) {

    fun getGyms(lat: Double, lng: Double, country: String) {
        apiService.getNearbyGyms(country, lat, lng, 50 * 1000)
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
    }
}