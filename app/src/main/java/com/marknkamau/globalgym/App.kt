package com.marknkamau.globalgym

import android.app.Application
import android.arch.persistence.room.Room
import com.marknkamau.globalgym.data.auth.AuthService
import com.marknkamau.globalgym.data.auth.AuthServiceImpl
import com.marknkamau.globalgym.data.local.AppDatabase
import com.marknkamau.globalgym.data.local.PaperService
import com.marknkamau.globalgym.data.local.PaperServiceImpl
import com.marknkamau.globalgym.data.remote.ApiService
import com.marknkamau.globalgym.data.remote.NetworkProvider
import com.marknkamau.globalgym.data.repository.DataRepository
import com.marknkamau.globalgym.data.repository.DataRepositoryImpl
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
        lateinit var dataRepository: DataRepository
    }

    override fun onCreate() {
        super.onCreate()

        Timber.plant(object : Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement): String {
                return "Timber/${element.fileName.substringBefore(".")}.${element.methodName}(Ln${element.lineNumber})"
            }
        })

        val appDatabase = Room.databaseBuilder(this, AppDatabase::class.java, "local-cache")
                .fallbackToDestructiveMigration()
                .build()

        val networkProvider = NetworkProvider()

        authService = AuthServiceImpl()
        val apiService = networkProvider.apiService
        val paperService = PaperServiceImpl(this)

        dataRepository = DataRepositoryImpl(appDatabase, apiService, paperService)

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