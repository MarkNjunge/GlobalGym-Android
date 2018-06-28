package com.marknkamau.globalgym.data.local

import android.content.Context
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
    private val LANGUAGE_KEY = "lang"

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
}
