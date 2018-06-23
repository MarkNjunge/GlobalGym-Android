package com.marknkamau.globalgym.utils

import com.marknkamau.globalgym.data.models.ApiError
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class NetworkUtils {
    private val apiErrorAdapter: JsonAdapter<ApiError>

    init {
        val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
        apiErrorAdapter = moshi.adapter(ApiError::class.java)
    }

    fun parseError(response: Response<*>): ApiError {
        return apiErrorAdapter.fromJson(response.errorBody()!!.string()) ?: ApiError("Error")
    }
}