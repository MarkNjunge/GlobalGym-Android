package com.marknkamau.globalgym.ui.activity.signup

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.marknkamau.globalgym.App
import com.marknkamau.globalgym.R
import com.marknkamau.globalgym.ui.activity.login.LoginActivity
import com.marknkamau.globalgym.ui.activity.register.RegisterActivity
import com.marknkamau.globalgym.utils.trimmedText
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity(), SignUpView {
    private lateinit var presenter: SignUpPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        presenter = SignUpPresenter(this, App.authService)

        tvLogin.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
            finish()
        }

        btnSignUp.setOnClickListener {
            signUp()
        }
    }

    override fun displayMessage(message: String) {
        pbLoading.visibility = View.GONE
        Toast.makeText(this@SignUpActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSignedUp() {
        startActivity(Intent(this@SignUpActivity, RegisterActivity::class.java))
        finish()
    }

    private fun signUp(){
        val email = etEmail.trimmedText
        if (email.isEmpty()){
            etEmail.error = "Required"
            return
        }

        val password = etPassword.trimmedText
        if (password.isEmpty()){
            etPassword.error = "Required"
            return
        }

        val passwordRetype = etPasswordRetype.trimmedText
        if (passwordRetype.isEmpty()){
            etPasswordRetype.error = "Required"
            return
        }

        if (password != passwordRetype){
            etPassword.error = "Passwords do not match"
            etPasswordRetype.error = "Passwords do not match"
            return
        }

        presenter.signUp(email, password)
        pbLoading.visibility = View.VISIBLE
    }

}
