package com.marknkamau.globalgym.data.repository

import com.marknkamau.globalgym.data.models.Gym
import com.marknkamau.globalgym.data.models.Instructor
import com.marknkamau.globalgym.data.remote.ApiService
import io.reactivex.Single

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class GymRepositoryImpl(private val apiService: ApiService) : GymRepository {
    override fun getNearbyGyms(country: String, lat: Double, lng: Double, radius: Int): Single<List<Gym>> {
        return apiService.getNearbyGyms(country, lat, lng, radius)
    }

    override fun getGym(gymId: String): Single<Gym> {
        return apiService.getGym(gymId)
    }

    override fun searchGyms(name: String): Single<List<Gym>> {
        return apiService.searchGyms(name)
    }

    override fun getInstructors(instructorIds: List<String>): Single<List<Instructor>> {
        return apiService.getInstructors(instructorIds)
    }
}