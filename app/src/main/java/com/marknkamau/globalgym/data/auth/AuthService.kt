package com.marknkamau.globalgym.data.auth

import com.marknkamau.globalgym.data.models.AuthUser
import io.reactivex.Completable

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

interface AuthService{

    fun signUp(email: String, password: String): Completable

    fun logIn(email: String, password: String): Completable

    fun setPasswordReset(email: String)

    fun signOut()

    fun isSignedIn(): Boolean

    fun getUser(): AuthUser?
}