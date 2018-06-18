package com.marknkamau.globalgym.data.local

import android.content.Context
import com.marknkamau.globalgym.data.models.User
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
}