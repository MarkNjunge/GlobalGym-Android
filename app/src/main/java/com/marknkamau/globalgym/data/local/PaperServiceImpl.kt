package com.marknkamau.globalgym.data.local

import android.content.Context
import com.marknkamau.globalgym.data.models.Gym
import com.marknkamau.globalgym.data.models.User
import com.marknkamau.globalgym.utils.LocaleManager
import io.paperdb.Book
import io.paperdb.Paper

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class PaperServiceImpl(context: Context) : PaperService {

    private val book: Book
    private val USER_KEY = "user"
    private val GYM_KEY = "gym"
    private val LANGUAGE_KEY = "lang"
    private val COUNTRY_KEY = "country"

    init {
        Paper.init(context)
        book = Paper.book()
    }

    override fun getUser(): User? {
        return book.read(USER_KEY)
    }

    override fun saveUser(user: User) {
        book.write(USER_KEY, user)
    }

    override fun updateUser(user: User) {
        deleteUser()
        saveUser(user)
    }

    override fun deleteUser() {
        book.delete(USER_KEY)
    }

    override fun getPreferredGym(): Gym? {
        return book.read(GYM_KEY)
    }

    override fun savePreferredGym(gym: Gym) {
        book.write(GYM_KEY, gym)
    }

    override fun updatePreferredGym(gym: Gym) {
        deletePreferredGym()
        savePreferredGym(gym)
    }

    override fun deletePreferredGym() {
        book.delete(GYM_KEY)
    }

    override fun getLanguageCode(): String {
        book.read<String>(LANGUAGE_KEY)?.let {
            return it
        }

        saveLanguageCode(LocaleManager.LANG_ENGLISH)
        return LocaleManager.LANG_ENGLISH
    }

    override fun saveLanguageCode(code: String) {
        book.write(LANGUAGE_KEY, code)
    }

    override fun getCurrentCountry(): String {
        return book.read(COUNTRY_KEY) ?: "Kenya"
    }

    override fun saveCurrentCountry(country: String) {
        book.write(COUNTRY_KEY, country)
    }
}
