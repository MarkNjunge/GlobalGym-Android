package com.marknkamau.globalgym.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

@Parcelize
data class Exercise(var stepIndex: Int, val title: String, val reps: String, val sets: Int) : Parcelable