package com.marknkamau.globalgym.data.remote

import com.marknkamau.globalgym.data.models.Gym
import com.marknkamau.globalgym.data.models.Instructor
import com.marknkamau.globalgym.data.models.User
import io.reactivex.Single
import retrofit2.http.*

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

interface ApiService {
    @GET("users/{id}")
    fun getUser(@Path("id") userId: String): Single<User>

    @POST("users/register")
    fun registerUser(@Body user: User): Single<User>

    @GET("gyms/nearby")
    fun getNearbyGyms(@Query("country") country: String,
                      @Query("lat") lat: Double,
                      @Query("lng") lng: Double,
                      @Query("radius") radius: Int): Single<List<Gym>>

    @POST("instructors")
    fun getInstructors(@Body instructorIds: List<String>): Single<List<Instructor>>
}