package com.marknkamau.globalgym.ui.activity.gymdetail

import com.marknkamau.globalgym.data.local.PaperService
import com.marknkamau.globalgym.data.models.Gym
import com.marknkamau.globalgym.data.models.User
import com.marknkamau.globalgym.data.models.UserPreferredGym
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

class GymDetailPresenter(private val view: GymDetailView,
                         private val gym: Gym,
                         private val paperService: PaperService,
                         private val apiService: ApiService) {

    private val user: User = paperService.getUser()!!

    init {
        if (user.preferredGym == gym.gymId) {
            view.onGymIsPreferred()
        }
    }

    private val compositeDisposable = CompositeDisposable()

    fun getInstructors(instructors: List<String>) {
        val disposable = apiService.getInstructors(instructors)
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
        val disposable = apiService.updateUser(UserPreferredGym(user.userId, gym.gymId))
                .compose(RxUtils.applySingleSchedulers())
                .subscribeBy(
                        onSuccess = {
                            val copy = user.copy(preferredGym = gym.gymId)
                            paperService.saveUser(copy)
                            paperService.savePreferredGym(gym)
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