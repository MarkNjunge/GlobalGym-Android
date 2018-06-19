package com.marknkamau.globalgym.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.marknkamau.globalgym.App

import com.marknkamau.globalgym.R
import com.marknkamau.globalgym.data.models.User
import com.marknkamau.globalgym.ui.login.LoginActivity
import kotlinx.android.synthetic.main.fragment_profile.*
import timber.log.Timber

class ProfileFragment : Fragment(), ProfileView {

    private lateinit var presenter: ProfilePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = ProfilePresenter(this, App.authService, App.paperService, App.networkProvider)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLogout.setOnClickListener {
            App.authService.signOut()
            startActivity(Intent(activity, LoginActivity::class.java))
            activity!!.finish()
        }
    }

    override fun onResume() {
        super.onResume()

        presenter.getUser()
    }

    override fun displayMessage(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("SetTextI18n")
    override fun onUserRetrieved(user: User) {
        tvName.text = user.firstName + " " + user.lastName
        tvEmail.text = user.email
        tvPhone.text = user.phone
        tvYear.text = user.yearOfBirth.toString()
        tvCountry.text = user.country
        tvGender.text = user.genderFull
        tvWeight.text = user.weight.toString() + "kg"
        tvTargetWeight.text = user.targetWeight.toString() + "kg"

        Glide.with(this@ProfileFragment)
                .load(user.profilePhoto)
                .into(imgProfileIcon)
    }

}
