package com.marknkamau.globalgym.data.models

import android.os.Parcelable
import com.marknkamau.globalgym.utils.MoshiAdapters
import kotlinx.android.parcel.Parcelize

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

@Parcelize
data class Session(val sessionId: String,
                   val userId: String,
                   val sessionName: String,
                   val dateTime: Long,
                   val gymId: String,
                   val sessionSteps: List<Exercise>,
                   val completed: Boolean = false) : Parcelable {

    fun toRoomSession(): RoomSession {
        val sessionSteps = MoshiAdapters.exerciseListAdapter.toJson(sessionSteps)
        return RoomSession(
                sessionId,
                userId,
                sessionName,
                dateTime,
                gymId,
                sessionSteps,
                completed)
    }
}