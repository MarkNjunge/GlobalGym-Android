package com.marknkamau.globalgym.data.repository

import com.marknkamau.globalgym.data.local.PaperService
import com.marknkamau.globalgym.data.local.SessionsDao
import com.marknkamau.globalgym.data.models.Session
import com.marknkamau.globalgym.data.models.SessionCompleted
import com.marknkamau.globalgym.data.remote.ApiService
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class SessionsRepositoryImpl(private val apiService: ApiService, private val sessionsDao: SessionsDao, private val paperService: PaperService) : SessionsRepository {
    override fun getSessions(): Observable<List<Session>> {
        return Observable.concatArray(getSessionsFromDb(), getSessionsFromApi())
    }

    override fun createSession(session: Session): Single<Session> {
        return apiService.createSession(session)
    }

    override fun setSessionCompleted(sessionCompleted: SessionCompleted): Completable {
        return apiService.setSessionCompleted(sessionCompleted)
                .flatMapCompletable { apiResponse ->
                    Timber.d(apiResponse.toString())
                    Completable.complete()
                }
    }

    override fun deleteSession(sessionId: String): Completable {
        return apiService.deleteSession(sessionId)
                .flatMapCompletable { apiResponse ->
                    Timber.d(apiResponse.toString())
                    Completable.complete()
                }
    }

    private fun getSessionsFromDb(): Observable<List<Session>> {
        return sessionsDao.getSessions()
                .filter { it.isNotEmpty() }
                .map {
                    it.map { roomSession -> roomSession.toSession() }
                }
                .toObservable()
                .doOnNext { Timber.d("Dispatching ${it.size} sessions from DB...") }
    }

    private fun getSessionsFromApi(): Observable<List<Session>> {
        paperService.getUser()?.let {
            return apiService.getSessions(it.userId)
                    .toObservable()
                    .doOnNext {
                        Timber.d("Dispatching ${it.size} sessions from API...")
                        storeSessions(it)
                    }
        }
        return Observable.error(Throwable("User is not signed in"))
    }

    private fun storeSessions(sessions: List<Session>) {
        val roomSessions = sessions.map { session -> session.toRoomSession() }
        Observable.fromCallable {
            sessionsDao.deleteAll()
            sessionsDao.insertAll(roomSessions)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe { Timber.d("Inserted ${sessions.size} sessions from API in DB...") }
    }
}