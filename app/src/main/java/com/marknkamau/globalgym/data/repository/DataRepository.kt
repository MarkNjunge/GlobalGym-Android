package com.marknkamau.globalgym.data.repository

import com.marknkamau.globalgym.data.local.PaperService
import com.marknkamau.globalgym.data.models.Session
import com.marknkamau.globalgym.data.remote.ApiService
import io.reactivex.Observable

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

interface DataRepository {
    // Temporary
    val apiService: ApiService
    val paperService: PaperService

    fun getSessions(): Observable<List<Session>>

}