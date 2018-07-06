package com.marknkamau.globalgym.utils

import com.marknkamau.globalgym.data.models.Exercise
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

object MoshiAdapters {
    private val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    val exerciseListAdapter: JsonAdapter<List<Exercise>> by lazy {
        val exerciseListType = Types.newParameterizedType(List::class.java, Exercise::class.java)
        moshi.adapter<List<Exercise>>(exerciseListType)
    }

}