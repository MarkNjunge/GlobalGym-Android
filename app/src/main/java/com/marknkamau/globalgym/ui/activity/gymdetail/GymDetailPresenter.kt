package com.marknkamau.globalgym.ui.activity.gymdetail

import com.marknkamau.globalgym.data.models.Gym
import com.marknkamau.globalgym.data.models.User
import com.marknkamau.globalgym.data.repository.GymRepository
import com.marknkamau.globalgym.data.repository.UserRepository
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

    fun getInstructors(instructors: List<String>) {
        val disposable = gymRepository.getInstructors(instructors)
                .compose(RxUtils.applySingleSchedulers())
                .subscribeBy(
                        onSuccess = {
                            view.onInstructorsReceived(it)
                            Timber.d(it.toString())
                        },
                        onError = onError
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
                        onError = onError
                )

        compositeDisposable.add(disposable)
    }

    fun clearDisposables() {
        compositeDisposable.clear()
    }

}