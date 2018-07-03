package com.marknkamau.globalgym.data.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

data class SuggestedSession(val name: String, val exercises: List<Exercise>) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.createTypedArrayList(Exercise.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeTypedList(exercises)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SuggestedSession> = object : Parcelable.Creator<SuggestedSession> {
            override fun createFromParcel(source: Parcel): SuggestedSession = SuggestedSession(source)
            override fun newArray(size: Int): Array<SuggestedSession?> = arrayOfNulls(size)
        }
    }
}