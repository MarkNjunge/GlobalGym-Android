package com.marknkamau.globalgym.ui.gyms

import com.marknkamau.globalgym.data.repository.GymRepository
import com.marknkamau.globalgym.data.repository.SettingsRepository
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

class GymsPresenter(private val view: GymsView,
                    private val gymRepository: GymRepository,
                    private val settingsRepository: SettingsRepository) {

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
                        onError = onError

                )

        compositeDisposable.add(disposable)
    }

    fun clearDisposables() {
        compositeDisposable.clear()
    }
}