package com.marknkamau.globalgym.data.models

import android.os.Parcel
import android.os.Parcelable

data class User(
        val userId: String,
        val firstName: String,
        val lastName: String,
        val email: String,
        val phone: String,
        val profilePhoto: String,
        val yearOfBirth: Int,
        val country: String,
        val gender: String,
        val weight: Int,
        val targetWeight: Int,
        val preferredGym: String?
) : Parcelable {
    val genderFull: String
        get() = if (gender == "M") "Male" else "Female"

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readInt(),
            source.readInt(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(userId)
        writeString(firstName)
        writeString(lastName)
        writeString(email)
        writeString(phone)
        writeString(profilePhoto)
        writeInt(yearOfBirth)
        writeString(country)
        writeString(gender)
        writeInt(weight)
        writeInt(targetWeight)
        writeString(preferredGym)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(source: Parcel): User = User(source)
            override fun newArray(size: Int): Array<User?> = arrayOfNulls(size)
        }
    }
}
