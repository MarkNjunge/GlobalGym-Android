package com.marknkamau.globalgym.data.remote

import com.marknkamau.globalgym.data.models.User
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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
}