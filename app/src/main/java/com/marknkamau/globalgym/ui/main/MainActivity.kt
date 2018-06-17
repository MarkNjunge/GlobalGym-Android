package com.marknkamau.globalgym.ui.main

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.marknkamau.globalgym.App
import com.marknkamau.globalgym.R
import com.marknkamau.globalgym.ui.login.LoginActivity
import com.marknkamau.globalgym.ui.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainView {
    private lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(this, App.authService, App.networkProvider)

        button.setOnClickListener {
            App.authService.signOut()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.checkIfSignedIn()
    }

    override fun onNotSignedIn() {
        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        finish()
    }

    override fun onNotRegistered() {
        startActivity(Intent(this@MainActivity, RegisterActivity::class.java))
        finish()
    }
}
