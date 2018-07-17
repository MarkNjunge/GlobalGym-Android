package com.marknkamau.globalgym.data.repository

import com.marknkamau.globalgym.data.models.Gym
import com.marknkamau.globalgym.data.models.Instructor
import io.reactivex.Single

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

interface GymRepository {
    fun getNearbyGyms(country: String, lat: Double, lng: Double, radius: Int): Single<List<Gym>>

    fun getGym(gymId: String): Single<Gym>

    fun searchGyms(name: String): Single<List<Gym>>

    fun getInstructors(instructorIds: List<String>): Single<List<Instructor>>
}