package com.marknkamau.globalgym.utils

import android.widget.EditText

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

val EditText.trimmedText: String
    get() = this.text.toString().trim()
