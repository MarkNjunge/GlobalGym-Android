package com.marknkamau.globalgym.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import com.marknkamau.globalgym.App
import com.marknkamau.globalgym.utils.LocaleManager

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context) {
        val languageCode = App.paperService.getLanguageCode()
        super.attachBaseContext(LocaleManager.updateResources(newBase, languageCode))
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        val languageCode = App.paperService.getLanguageCode()
        LocaleManager.updateResources(this, languageCode)
    }
}
