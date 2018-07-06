package com.marknkamau.globalgym.data.models

import android.os.Parcel
import android.os.Parcelable
import com.marknkamau.globalgym.utils.MoshiAdapters

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

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

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readLong(),
            source.readString(),
            source.createTypedArrayList(Exercise.CREATOR),
            1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(sessionId)
        writeString(userId)
        writeString(sessionName)
        writeLong(dateTime)
        writeString(gymId)
        writeTypedList(sessionSteps)
        writeInt((if (completed) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Session> = object : Parcelable.Creator<Session> {
            override fun createFromParcel(source: Parcel): Session = Session(source)
            override fun newArray(size: Int): Array<Session?> = arrayOfNulls(size)
        }
    }
}