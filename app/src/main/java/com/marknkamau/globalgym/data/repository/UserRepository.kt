package com.marknkamau.globalgym.data.repository

import com.marknkamau.globalgym.data.models.Gym
import com.marknkamau.globalgym.data.models.User
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

interface UserRepository {
    fun getCurrentUser(): User?

    fun setCurrentUser(user: User): Completable

    fun getUser(userId: String): Single<User>

    fun registerUser(user: User): Single<User>

    fun updateUser(user: User): Single<User>

    fun getPreferredGym(): Gym?

    fun updatePreferredGym(gym: Gym): Completable

    fun deletePreferredGymLocal()

    fun deleteUserLocal()
}