package com.marknkamau.globalgym

import android.app.Application
import com.marknkamau.globalgym.data.auth.AuthService
import com.marknkamau.globalgym.data.auth.AuthServiceImpl
import com.marknkamau.globalgym.data.local.PaperService
import com.marknkamau.globalgym.data.local.PaperServiceImpl
import com.marknkamau.globalgym.data.remote.ApiService
import com.marknkamau.globalgym.data.remote.NetworkProvider
import com.marknkamau.globalgym.utils.NetworkUtils
import com.marknkamau.globalgym.utils.RxUtils
import com.marknkamau.globalgym.utils.maps.LocationUtils
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class App : Application() {

    companion object {
        lateinit var authService: AuthService
        lateinit var apiService: ApiService
        lateinit var paperService: PaperService
    }

    override fun onCreate() {
        super.onCreate()

        Timber.plant(object : Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement): String {
                return "Timber/${element.fileName.substringBefore(".")}.${element.methodName}(Ln${element.lineNumber})"
            }
        })

        val networkProvider = NetworkProvider()

        authService = AuthServiceImpl()
        apiService = networkProvider.apiService
        paperService = PaperServiceImpl(this)

        val locationUtils = LocationUtils(this)
        locationUtils.getCurrentLocation()
                .flatMap { location ->
                    locationUtils.reverseGeocode(location.latitude, location.longitude)
                }
                .compose(RxUtils.applySingleSchedulers())
                .subscribeBy(
                        onSuccess = { address ->
                            paperService.saveCurrentCountry(address.countryName)
                        },
                        onError = {
                            Timber.e(it)
                        }
                )
    }

}