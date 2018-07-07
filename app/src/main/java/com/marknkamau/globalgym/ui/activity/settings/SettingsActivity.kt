package com.marknkamau.globalgym.ui.activity.settings

import android.content.Intent
import android.os.Bundle
import com.marknkamau.globalgym.App
import com.marknkamau.globalgym.BuildConfig
import com.marknkamau.globalgym.R
import com.marknkamau.globalgym.ui.activity.BaseActivity
import com.marknkamau.globalgym.ui.activity.login.LoginActivity
import com.marknkamau.globalgym.ui.activity.main.MainActivity
import com.marknkamau.globalgym.utils.LocaleManager
import com.marknkamau.globalgym.utils.RxUtils
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.title = getString(R.string.settings)

        val dialog = SelectLanguageDialog()
        dialog.onSelected = { language ->
            val code = LocaleManager.getCode(language)

            App.dataRepository.paperService.saveLanguageCode(code)

            LocaleManager.updateResources(this, code)

            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        val code = App.dataRepository.paperService.getLanguageCode()
        tvCurrentLanguage.text = LocaleManager.getLanguage(code)

        layoutLanguage.setOnClickListener {
            dialog.show(supportFragmentManager, "lang")
        }

        layoutLogout.setOnClickListener {
            App.dataRepository.clearUserCache()
                    .compose(RxUtils.applyCompletableSchedulers())

            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }

        tvVersion.text = getString(R.string.version, BuildConfig.VERSION_NAME)

    }
}
