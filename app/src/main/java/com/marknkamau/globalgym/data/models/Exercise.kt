package com.marknkamau.globalgym.data.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

data class Exercise(var stepIndex: Int, val title: String, val reps: String, val sets: Int) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(stepIndex)
        writeString(title)
        writeString(reps)
        writeInt(sets)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Exercise> = object : Parcelable.Creator<Exercise> {
            override fun createFromParcel(source: Parcel): Exercise = Exercise(source)
            override fun newArray(size: Int): Array<Exercise?> = arrayOfNulls(size)
        }
    }
}