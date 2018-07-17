package com.marknkamau.globalgym.data.repository

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

interface SettingsRepository{
    fun getLanguageCode(): String

    fun saveLanguageCode(code: String)

    fun getCurrentCountry(): String

    fun saveCurrentCountry(country: String)
}