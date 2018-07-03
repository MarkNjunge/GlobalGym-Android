package com.marknkamau.globalgym.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import io.reactivex.Observable
import io.reactivex.annotations.NonNull

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

object RxSearch {

    fun fromEditText(@NonNull editText: EditText): Observable<String> {
        return Observable.create { emitter ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence, after: Int, before: Int, count: Int) {
                    emitter.onNext(s.toString())
                }
            })
        }
    }
}