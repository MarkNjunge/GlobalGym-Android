package com.marknkamau.globalgym.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
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

}
