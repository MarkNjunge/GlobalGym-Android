package com.marknkamau.globalgym.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

@Parcelize
data class SuggestedSession(val name: String, val exercises: List<Exercise>) : Parcelable