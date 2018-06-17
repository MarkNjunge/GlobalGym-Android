package com.marknkamau.globalgym.ui.login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.marknkamau.globalgym.App
import com.marknkamau.globalgym.R
import com.marknkamau.globalgym.ui.main.MainActivity
import com.marknkamau.globalgym.ui.register.RegisterActivity
import com.marknkamau.globalgym.ui.signup.SignUpActivity
import com.marknkamau.globalgym.utils.trimmedText
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginView {
    private lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        presenter = LoginPresenter(this, App.authService, App.networkProvider)

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

    override fun displayMessage(message: String) {
        pbLoading.visibility = View.GONE
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
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

        displayMessage("Password reset email sent")
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
