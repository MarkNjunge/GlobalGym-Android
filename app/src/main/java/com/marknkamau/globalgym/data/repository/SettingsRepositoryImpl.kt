package com.marknkamau.globalgym.data.repository

import com.marknkamau.globalgym.data.local.PaperService

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class SettingsRepositoryImpl(private val paperService: PaperService) : SettingsRepository {
    override fun getLanguageCode(): String {
        return paperService.getLanguageCode()
    }

    override fun saveLanguageCode(code: String) {
        return paperService.saveLanguageCode(code)
    }

    override fun getCurrentCountry(): String {
        return paperService.getCurrentCountry()
    }

    override fun saveCurrentCountry(country: String) {
        return paperService.saveCurrentCountry(country)
    }
}