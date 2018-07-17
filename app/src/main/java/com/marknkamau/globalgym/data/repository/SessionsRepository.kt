package com.marknkamau.globalgym.data.repository

import com.marknkamau.globalgym.data.models.Session
import com.marknkamau.globalgym.data.models.SessionCompleted
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

interface SessionsRepository {
    fun getSessions(): Observable<List<Session>>

    fun createSession(session: Session): Single<Session>

    fun setSessionCompleted(sessionCompleted: SessionCompleted): Completable

    fun deleteSession(sessionId: String): Completable
}