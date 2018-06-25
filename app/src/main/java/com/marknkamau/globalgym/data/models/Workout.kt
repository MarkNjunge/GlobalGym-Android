package com.marknkamau.globalgym.data.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

data class Workout(val userId: String, val sessionName: String, val dateTime: Long, val gymId: String, val workoutSteps: List<Exercise>) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readLong(),
            source.readString(),
            source.createTypedArrayList(Exercise.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(userId)
        writeString(sessionName)
        writeLong(dateTime)
        writeString(gymId)
        writeTypedList(workoutSteps)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Workout> = object : Parcelable.Creator<Workout> {
            override fun createFromParcel(source: Parcel): Workout = Workout(source)
            override fun newArray(size: Int): Array<Workout?> = arrayOfNulls(size)
        }
    }
}