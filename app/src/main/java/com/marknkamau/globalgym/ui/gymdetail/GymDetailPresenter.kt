package com.marknkamau.globalgym.ui.gymdetail

import com.marknkamau.globalgym.data.remote.ApiService
import com.marknkamau.globalgym.utils.RxUtils
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class GymDetailPresenter(private val view: GymDetailView,private val apiService: ApiService){

    fun getInstructors(instructors:List<String>){
        apiService.getInstructors(instructors)
                .compose(RxUtils.applySingleSchedulers())
                .subscribeBy(
                        onSuccess = {
                            view.onInstructorsReceived(it)
                            Timber.d(it.toString())
                        },
                        onError = {
                            Timber.e(it)
                            view.displayMessage(it.message ?: "Error getting instructors")
                        }
                )
    }

}