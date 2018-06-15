package com.marknkamau.globalgym.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.marknkamau.globalgym.data.models.AuthUser
import com.marknkamau.globalgym.utils.RxUtils
import io.reactivex.Completable

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class AuthServiceImpl : AuthService {

    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun signUp(email: String, password: String): Completable {
        return Completable.create { emitter ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener { emitter.onComplete() }
                    .addOnFailureListener { emitter.onError(it) }
        }.compose(RxUtils.applyCompletableSchedulers())
    }

    override fun logIn(email: String, password: String): Completable {
        return Completable.create { emitter ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener { emitter.onComplete() }
                    .addOnFailureListener { emitter.onError(it) }
        }.compose(RxUtils.applyCompletableSchedulers())
    }

    override fun setPasswordReset(email: String) {
        firebaseAuth.sendPasswordResetEmail(email)
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

    override fun isSignedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun getUser(): AuthUser? {
        return if (firebaseAuth.currentUser == null) {
            null
        } else {
            AuthUser(firebaseAuth.currentUser!!.uid, firebaseAuth.currentUser!!.email!!)
        }
    }

}