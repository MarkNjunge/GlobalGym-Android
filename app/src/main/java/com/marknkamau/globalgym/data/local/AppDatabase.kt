package com.marknkamau.globalgym.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.marknkamau.globalgym.data.models.RoomSession

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

@Database(entities = [RoomSession::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionsDao(): SessionsDao
}