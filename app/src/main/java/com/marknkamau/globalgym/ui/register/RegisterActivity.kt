package com.marknkamau.globalgym.ui.register

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.marknkamau.globalgym.App
import com.marknkamau.globalgym.R
import com.marknkamau.globalgym.ui.main.MainActivity
import com.marknkamau.globalgym.utils.trimmedText
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(), RegisterView {
    private var country = ""

    private lateinit var presenter: RegisterPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        presenter = RegisterPresenter(this, App.authService, App.networkProvider)

        btnContinue.setOnClickListener {
            registerUser()
        }

        val countryDialog = CountryDialog()
        countryDialog.onSelected {
            country = it
            tvCountry.text = it
        }

        tvCountry.setOnClickListener {
            countryDialog.show(supportFragmentManager, "Select country")
        }
    }

    override fun displayMessage(message: String) {
        pbLoading.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onRegistered() {
        startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
        finish()
    }

    private fun registerUser() {
        val firstName = etFirstName.trimmedText
        val lastName = etLastName.trimmedText
        val phone = etPhone.trimmedText
        val year = etYearBirth.trimmedText
        val weight = etWeight.trimmedText
        val targetWeight = etTargetWeight.trimmedText
        val gender = findViewById<RadioButton>(rgGender.checkedRadioButtonId).text.toString()

        if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || year.isEmpty() || weight.isEmpty() || targetWeight.isEmpty() || country.isEmpty()) {
            return
        }

        presenter.registerUser(firstName,lastName, phone, year.toInt(), country, gender, weight.toInt(), targetWeight.toInt())
        pbLoading.visibility = View.VISIBLE
    }
}
