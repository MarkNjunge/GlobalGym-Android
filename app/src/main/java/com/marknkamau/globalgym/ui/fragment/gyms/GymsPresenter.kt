package com.marknkamau.globalgym.ui.fragment.gyms

import com.marknkamau.globalgym.data.repository.GymRepository
import com.marknkamau.globalgym.data.repository.SettingsRepository
import com.marknkamau.globalgym.utils.RxUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class GymsPresenter(private val view: GymsView,
                    private val gymRepository: GymRepository,
                    private val settingsRepository: SettingsRepository) {

    private val compositeDisposable = CompositeDisposable()

    fun getGyms(lat: Double, lng: Double) {
        val disposable = gymRepository.getNearbyGyms(settingsRepository.getCurrentCountry(), lat, lng, 50 * 1000)
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

    fun clearDisposables() {
        compositeDisposable.clear()
    }
}