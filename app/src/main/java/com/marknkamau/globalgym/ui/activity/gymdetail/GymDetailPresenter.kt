package com.marknkamau.globalgym.ui.activity.gymdetail

import com.marknkamau.globalgym.data.local.PaperService
import com.marknkamau.globalgym.data.models.Gym
import com.marknkamau.globalgym.data.models.User
import com.marknkamau.globalgym.data.models.UserPreferredGym
import com.marknkamau.globalgym.data.remote.ApiService
import com.marknkamau.globalgym.utils.RxUtils
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

    fun getInstructors(instructors: List<String>) {
        apiService.getInstructors(instructors)
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
    }

    fun setGymAsPreferred() {
        apiService.updateUser(UserPreferredGym(user.userId, gym.gymId))
                .compose(RxUtils.applySingleSchedulers())
                .subscribeBy(
                        onSuccess = {
                            val copy = user.copy(preferredGym = gym.gymId)
                            paperService.saveUser(copy)
                            view.displayMessage("Gym set as preferred")
                            view.onGymIsPreferred()
                        },
                        onError = {
                            Timber.e(it)
                            view.displayMessage(it.message ?: "Error setting as preferred gym")
                        }
                )

    }

}