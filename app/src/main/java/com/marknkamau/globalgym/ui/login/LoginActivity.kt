package com.marknkamau.globalgym.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.marknkamau.globalgym.App
import com.marknkamau.globalgym.R
import com.marknkamau.globalgym.ui.base.BaseActivity
import com.marknkamau.globalgym.ui.main.MainActivity
import com.marknkamau.globalgym.ui.register.RegisterActivity
import com.marknkamau.globalgym.ui.signup.SignUpActivity
import com.marknkamau.globalgym.utils.trimmedText
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), LoginView {
    private lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        presenter = LoginPresenter(this, App.authService, App.userRepository)

        tvCreateAccount.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
        }

        tvForgotPassword.setOnClickListener {
            sendPasswordRest()
        }

        btnLogin.setOnClickListener {
            login()
        }
    }

    override fun onStop() {
        super.onStop()
        presenter.clearDisposables()
    }

    override fun displayNoInternetMessage() {
        pbLoading.visibility = View.GONE
        Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show()
    }

    override fun displayDefaultErrorMessage() {
        pbLoading.visibility = View.GONE
        Toast.makeText(this, R.string.an_error_has_occurred, Toast.LENGTH_SHORT).show()
    }

    override fun displayMessage(message: String) {
        pbLoading.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onLoggedIn() {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }

    override fun onNotRegistered() {
        startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        finish()
    }

    private fun sendPasswordRest() {
        val email = etEmail.trimmedText
        if (email.isEmpty()) {
            etEmail.error = "Required"
            return
        }

        presenter.sendPasswordReset(email)
    }

    private fun login() {
        val email = etEmail.trimmedText
        if (email.isEmpty()) {
            etEmail.error = "Required"
            return
        }

        val password = etPassword.trimmedText
        if (password.isEmpty()) {
            etPassword.error = "Required"
            return
        }

        presenter.logIn(email, password)
        pbLoading.visibility = View.VISIBLE
    }

}
