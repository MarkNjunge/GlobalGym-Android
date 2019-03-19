package com.marknkamau.globalgym.ui.settings

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import com.marknkamau.globalgym.R
import com.marknkamau.globalgym.utils.LocaleManager

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class SelectLanguageDialog : DialogFragment() {

    var onSelected: ((language: String) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())

        builder.setTitle(requireContext().getString(R.string.select_language))
        builder.setItems(LocaleManager.languages) { _, which ->
            onSelected?.invoke(LocaleManager.languages[which])
        }

        return builder.create()
    }

}