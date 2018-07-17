package com.marknkamau.globalgym.ui.activity.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.marknkamau.globalgym.App
import com.marknkamau.globalgym.R
import com.marknkamau.globalgym.ui.activity.BaseActivity
import com.marknkamau.globalgym.ui.activity.main.MainActivity
import com.marknkamau.globalgym.ui.fragment.userDetails.UserDetailsFragment
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity(), RegisterView {
    private lateinit var presenter: RegisterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        presenter = RegisterPresenter(this, App.authService, App.userRepository)

        val userDetailsFragment = UserDetailsFragment()
        userDetailsFragment.onComplete = {
            val (firstName, lastName, phone, year, country, gender, weight, targetWeight) = it

            presenter.registerUser(firstName, lastName, phone, year, country, gender, weight, targetWeight)
            pbLoading.visibility = View.VISIBLE
        }

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_holder, userDetailsFragment, "user_details")
                .commit()
    }

    override fun onStop() {
        super.onStop()
        presenter.clearDisposables()
    }

    override fun displayMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onRegistered() {
        startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
        finish()
    }

}
