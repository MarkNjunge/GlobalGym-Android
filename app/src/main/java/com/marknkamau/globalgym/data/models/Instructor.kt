package com.marknkamau.globalgym.data.models

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

data class Instructor(val instructorId: String,
                      val firstName: String,
                      val lastName: String,
                      val profilePhoto: String,
                      val email: String,
                      val yearOfBirth: Int,
                      val phone: String,
                      val gender: String,
                      val country: String) {
    val genderFull: String
        get() = if (gender == "M") "Male" else "Female"
}