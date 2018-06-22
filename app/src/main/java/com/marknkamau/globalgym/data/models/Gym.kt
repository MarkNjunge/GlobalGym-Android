package com.marknkamau.globalgym.data.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

data class Gym(val gymId: String,
               val name: String,
               val logo: String,
               val phone: String,
               val website: String,
               val openTime: String,
               val closeTime: String,
               val country: String,
               val city: String,
               val images: List<String>,
               val cords: Cords) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.createStringArrayList(),
            // Manually written
            source.readParcelable<Cords>(Cords::class.java.classLoader)
//            source.readCords()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(gymId)
        writeString(name)
        writeString(logo)
        writeString(phone)
        writeString(website)
        writeString(openTime)
        writeString(closeTime)
        writeString(country)
        writeString(city)
        writeStringList(images)
        // Manually written
        writeParcelable(cords, flags)
//        writeCords(cords)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Gym> = object : Parcelable.Creator<Gym> {
            override fun createFromParcel(source: Parcel): Gym = Gym(source)
            override fun newArray(size: Int): Array<Gym?> = arrayOfNulls(size)
        }
    }
}
