package com.marknkamau.globalgym.data.repository

import com.marknkamau.globalgym.data.local.PaperService
import com.marknkamau.globalgym.data.models.Gym
import com.marknkamau.globalgym.data.models.User
import com.marknkamau.globalgym.data.models.UserPreferredGym
import com.marknkamau.globalgym.data.remote.ApiService
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class UserRepositoryImpl(private val apiService: ApiService, private val paperService: PaperService) : UserRepository {
    override fun getCurrentUser(): User? {
        return paperService.getUser()
    }

    override fun setCurrentUser(user: User): Completable {
        return Completable.create { emitter ->
            paperService.saveUser(user)
            emitter.onComplete()
        }
    }

    override fun getUser(userId: String): Single<User> {
        return apiService.getUser(userId)
    }

    override fun registerUser(user: User): Single<User> {
        return apiService.registerUser(user)
    }

    override fun updateUser(user: User): Single<User> {
        return apiService.updateUser(user)
                .doOnSuccess {
                    paperService.saveUser(user)
                }
    }

    override fun getPreferredGym(): Gym? {
        return paperService.getPreferredGym()
    }

    override fun updatePreferredGym(gym: Gym): Completable {
        paperService.getUser()?.let { user ->
            return apiService.updateUser(UserPreferredGym(user.userId, gym.gymId))
                    .flatMap {
                        val copy = user.copy(preferredGym = gym.gymId)
                        updateUser(copy)
                    }
                    .flatMapCompletable {
                        Completable.fromCallable { paperService.savePreferredGym(gym) }
                    }

        }
        return Completable.error(Exception("User is not logged in"))
    }

    override fun deletePreferredGymLocal() {
        paperService.deletePreferredGym()
    }

    override fun deleteUserLocal() {
        return paperService.deleteUser()
    }

}