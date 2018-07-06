package com.marknkamau.globalgym.data.repository

import com.marknkamau.globalgym.data.local.AppDatabase
import com.marknkamau.globalgym.data.local.PaperService
import com.marknkamau.globalgym.data.models.Session
import com.marknkamau.globalgym.data.remote.ApiService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class DataRepositoryImpl(private val appDatabase: AppDatabase, override val apiService: ApiService, override val paperService: PaperService) : DataRepository {
    override fun getSessions(): Observable<List<Session>> {
        return Observable.concatArray(getSessionsFromDb(), getSessionsFromApi())
    }

    private fun getSessionsFromDb(): Observable<List<Session>> {
        return appDatabase.sessionsDao().getSessions()
                .filter { it.isNotEmpty() }
                .map {
                    it.map { roomSession -> roomSession.toSession() }
                }
                .toObservable()
                .doOnNext { Timber.d("Dispatching ${it.size} sessions from DB...") }
    }

    private fun getSessionsFromApi(): Observable<List<Session>> {
        return apiService.getSessions(paperService.getUser()!!.userId)
                .toObservable()
                .doOnNext {
                    Timber.d("Dispatching ${it.size} sessions from API...")
                    storeSessions(it)
                }
    }

    private fun storeSessions(sessions: List<Session>) {
        val roomSessions = sessions.map { session -> session.toRoomSession() }
        Observable.fromCallable { appDatabase.sessionsDao().insertAll(roomSessions) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe { Timber.d("Inserted ${sessions.size} sessions from API in DB...") }
    }

}