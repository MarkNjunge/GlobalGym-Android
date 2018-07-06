package com.marknkamau.globalgym.data.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.marknkamau.globalgym.data.models.RoomSession
import io.reactivex.Single

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

@Dao
abstract class SessionsDao {
    @Query("SELECT * FROM sessions")
    abstract fun getSessions(): Single<List<RoomSession>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(sessions: List<RoomSession>)

}