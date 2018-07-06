package com.marknkamau.globalgym.ui.fragment.profile

import com.marknkamau.globalgym.data.auth.AuthService
import com.marknkamau.globalgym.data.local.PaperService
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

class ProfilePresenter(private val view: ProfileView,
                       private val authService: AuthService,
                       private val dataRepository: DataRepository) {


    private val compositeDisposable = CompositeDisposable()

    fun clearDisposables() {
        compositeDisposable.clear()
    }

    fun getUser() {
        dataRepository.paperService.getUser()?.let { user ->
            view.onUserRetrieved(user)
            user.preferredGym?.let {
                getGym(it)
            }
        }

        val disposable = dataRepository.apiService.getUser(authService.getUser()!!.id)
                .compose(RxUtils.applySingleSchedulers())
                .subscribeBy(
                        onSuccess = { user ->
                            view.onUserRetrieved(user)
                            dataRepository.paperService.saveUser(user)

                            user.preferredGym?.let {
                                getGym(it)
                            }
                        },
                        onError = {
                            view.displayMessage(it.message
                                    ?: "There was an error retrieving your profile")
                        }
                )

        compositeDisposable.add(disposable)
    }

    private fun getGym(gymId: String) {
        dataRepository.paperService.getPreferredGym()?.let {
            view.onGymRetrieved(it)
        }

        val disposable = dataRepository.apiService.getGym(gymId)
                .compose(RxUtils.applySingleSchedulers())
                .subscribeBy(
                        onSuccess = {
                            view.onGymRetrieved(it)
                            dataRepository.paperService.savePreferredGym(it)
                        },
                        onError = {
                            Timber.e(it)
                            view.displayMessage(it.message
                                    ?: "There was an error retrieving your preferred gym")
                        }
                )

        compositeDisposable.add(disposable)
    }

}