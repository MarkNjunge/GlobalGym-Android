package com.marknkamau.globalgym.data.remote

import com.marknkamau.globalgym.data.models.ApiError
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import okhttp3.ResponseBody
import retrofit2.Converter
import java.util.concurrent.TimeUnit


/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class NetworkProvider {
    private val BASE_URL = "https://global-gym.herokuapp.com/"
    val apiService: ApiService
    private val TIMEOUT = 60L
    private var converter: Converter<ResponseBody, ApiError>
    private val adapter by lazy {
        val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
        moshi.adapter(ApiError::class.java)
    }

    init {
        apiService = provideRetrofit(BASE_URL).create(ApiService::class.java)

        converter = provideRetrofit(BASE_URL).responseBodyConverter(ApiError::class.java, arrayOfNulls(0))
    }

    private fun provideRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(provideOkHttpClient())
                .build()
    }

    private fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC

        return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build()
    }


    fun parseError(response: Response<*>): ApiError {
        return adapter.fromJson(response.errorBody()!!.string()) ?: ApiError("Error")
    }
}