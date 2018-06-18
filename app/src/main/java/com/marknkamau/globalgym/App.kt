package com.marknkamau.globalgym

import android.app.Application
import com.marknkamau.globalgym.data.auth.AuthService
import com.marknkamau.globalgym.data.auth.AuthServiceImpl
import com.marknkamau.globalgym.data.local.PaperService
import com.marknkamau.globalgym.data.local.PaperServiceImpl
import com.marknkamau.globalgym.data.remote.NetworkProvider
import io.paperdb.Paper
import timber.log.Timber

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class App : Application() {

    companion object {
        lateinit var authService: AuthService
        lateinit var networkProvider: NetworkProvider
        lateinit var paperService: PaperService
    }

    override fun onCreate() {
        super.onCreate()

        Timber.plant(object : Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement): String {
                return "Timber/${element.fileName.substringBefore(".")}.${element.methodName}(Ln${element.lineNumber})"
            }
        })

        authService = AuthServiceImpl()
        networkProvider = NetworkProvider()
        paperService = PaperServiceImpl(this)
    }

}