package com.marknkamau.globalgym.data.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.marknkamau.globalgym.utils.MoshiAdapters

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

@Entity(tableName = "sessions")
data class RoomSession(
        @PrimaryKey
        val sessionId: String,
        val userId: String,
        val sessionName: String,
        val dateTime: Long,
        val gymId: String,
        val sessionSteps: String,
        val completed: Boolean = false) {

    fun toSession(): Session {
        val exercises = MoshiAdapters.exerciseListAdapter.fromJson(sessionSteps) ?: listOf()
        return Session(sessionId, userId, sessionName, dateTime, gymId, exercises, completed)
    }


}
