package com.marknkamau.globalgym.data.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

data class Cords(val lat: Double, val lng: Double) : Parcelable {
    constructor(source: Parcel) : this(
            source.readDouble(),
            source.readDouble()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeDouble(lat)
        writeDouble(lng)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Cords> = object : Parcelable.Creator<Cords> {
            override fun createFromParcel(source: Parcel): Cords = Cords(source)
            override fun newArray(size: Int): Array<Cords?> = arrayOfNulls(size)
        }
    }
}