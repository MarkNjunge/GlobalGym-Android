package com.marknkamau.globalgym.data.remote

import com.google.firebase.auth.FirebaseAuth
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor : Interceptor {
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firebaseToken by lazy {
        firebaseAuth.currentUser?.let { user ->
            user.getIdToken(false).result?.let { result ->
                return@lazy result.token
            }
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val builder = original.newBuilder()

        // Add a token if the user is signed in
        firebaseToken?.let { token ->
            builder.header("Authorization", token)
        }

        val request = builder.build()
        return chain.proceed(request)
    }
}