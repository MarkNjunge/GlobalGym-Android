package com.marknkamau.globalgym.ui.activity.gymdetail

import com.marknkamau.globalgym.data.models.Gym
import com.marknkamau.globalgym.data.models.User
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

class GymDetailPresenter(private val view: GymDetailView,
                         private val gym: Gym,
                         private val userRepository: UserRepository,
                         private val gymRepository: GymRepository) {

    // TODO Go to log in if null
    private val user: User = userRepository.getCurrentUser()!!

    init {
        if (user.preferredGym == gym.gymId) {
            view.onGymIsPreferred()
        }
    }

    private val compositeDisposable = CompositeDisposable()

    fun getInstructors(instructors: List<String>) {
        val disposable = gymRepository.getInstructors(instructors)
                .compose(RxUtils.applySingleSchedulers())
                .subscribeBy(
                        onSuccess = {
                            view.onInstructorsReceived(it)
                            Timber.d(it.toString())
                        },
                        onError = {
                            Timber.e(it)
                            view.displayMessage(it.message ?: "Error getting instructors")
                        }
                )

        compositeDisposable.add(disposable)
    }

    fun setGymAsPreferred() {
        val disposable = userRepository.updatePreferredGym(gym)
                .compose(RxUtils.applyCompletableSchedulers())
                .subscribeBy(
                        onComplete = {
                            view.displayMessage("Gym set as preferred")
                            view.onGymIsPreferred()
                        },
                        onError = {
                            Timber.e(it)
                            view.displayMessage(it.message ?: "Error setting as preferred gym")
                        }
                )

        compositeDisposable.add(disposable)
    }

    fun clearDisposables() {
        compositeDisposable.clear()
    }

}