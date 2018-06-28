package com.marknkamau.globalgym.utils

import android.content.Context
import android.content.res.Configuration
import java.util.*

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

object LocaleManager {
    private val CODE_ENGLISH = "en"
    private val CODE_SWAHILI = "sw"

    val LANG_ENGLISH = "English"
    val LANG_SWAHILI = "Kiswahili"

    val languages = arrayOf(LANG_ENGLISH, LANG_SWAHILI)

    fun getCode(language: String): String {
        return when (language) {
            LANG_ENGLISH -> CODE_ENGLISH
            LANG_SWAHILI -> CODE_SWAHILI
            else -> CODE_ENGLISH
        }
    }

    fun getLanguage(code: String): String {
        return when (code) {
            CODE_ENGLISH -> LANG_ENGLISH
            CODE_SWAHILI -> LANG_SWAHILI
            else -> LANG_ENGLISH
        }
    }

    // https://github.com/YarikSOffice/LanguageTest
    fun updateResources(context: Context, code: String): Context {
        val locale = Locale(code)
        Locale.setDefault(locale)

        val res = context.resources
        val config = Configuration(res.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }

}
