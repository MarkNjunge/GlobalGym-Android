package com.marknkamau.globalgym.data.local

import android.arch.persistence.room.*
import com.marknkamau.globalgym.data.models.RoomSession
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

@Dao
interface SessionsDao {
    @Query("SELECT * FROM sessions")
    fun getSessions(): Single<List<RoomSession>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(sessions: List<RoomSession>)

    @Query("DELETE FROM sessions")
    fun deleteAll()

}