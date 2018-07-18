package com.marknkamau.globalgym.ui.fragment.profile

import com.marknkamau.globalgym.data.auth.AuthService
import com.marknkamau.globalgym.data.repository.GymRepository
import com.marknkamau.globalgym.data.repository.UserRepository
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
                       private val userRepository: UserRepository,
                       private val gymRepository: GymRepository) {


    private val compositeDisposable = CompositeDisposable()

    fun clearDisposables() {
        compositeDisposable.clear()
    }

    fun getUser() {
        userRepository.getCurrentUser()?.let { user ->
            view.onUserRetrieved(user)
            user.preferredGym?.let {
                getGym(it)
            }
        }

        val disposable = userRepository.getUser(authService.getUser()!!.id)
                .compose(RxUtils.applySingleSchedulers())
                .subscribeBy(
                        onSuccess = { user ->
                            view.onUserRetrieved(user)

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
        userRepository.getPreferredGym()?.let {
            view.onGymRetrieved(it)
        }

        val disposable = gymRepository.getGym(gymId)
                .compose(RxUtils.applySingleSchedulers())
                .subscribeBy(
                        onSuccess = {
                            view.onGymRetrieved(it)
                            userRepository.updatePreferredGym(it)
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