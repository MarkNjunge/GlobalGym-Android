package com.marknkamau.globalgym.data.local

import com.marknkamau.globalgym.data.models.Gym
import com.marknkamau.globalgym.data.models.User

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

interface PaperService {
    fun getUser(): User?
    fun saveUser(user: User)
    fun updateUser(user: User)
    fun deleteUser()

    fun getPreferredGym(): Gym?
    fun savePreferredGym(gym: Gym)
    fun updatePreferredGym(gym: Gym)
    fun deletePreferredGym()

    fun getLanguageCode(): String
    fun saveLanguageCode(code: String)

    fun getCurrentCountry(): String
    fun saveCurrentCountry(country: String)
}