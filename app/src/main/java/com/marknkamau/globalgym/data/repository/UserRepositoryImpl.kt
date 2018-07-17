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

class UserRepositoryImpl(private val apiService: ApiService, private val paperService: PaperService) : UserRepository{
    override fun getCurrentUser(): User? {
       return paperService.getUser()
    }

    override fun setCurrentUser(userId: String): Single<User> {
        return apiService.getUser(userId)
                .doOnSuccess { user ->
                    paperService.saveUser(user)
                }
    }

    override fun registerUser(user: User): Single<User> {
        return apiService.registerUser(user)
    }

    override fun updateUser(user: User): Single<User> {
        return apiService.updateUser(user)
    }

    override fun getPreferredGym(): Gym? {
        return paperService.getPreferredGym()
    }

    override fun updatePreferredGym(gym: Gym): Completable {
        paperService.getUser()?.let { user ->
            return Completable.fromSingle(apiService.updateUser(UserPreferredGym(user.userId, gym.gymId)))
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