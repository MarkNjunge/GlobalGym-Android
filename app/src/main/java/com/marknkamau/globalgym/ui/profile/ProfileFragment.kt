package com.marknkamau.globalgym.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.marknkamau.globalgym.App

import com.marknkamau.globalgym.R
import com.marknkamau.globalgym.data.models.Gym
import com.marknkamau.globalgym.data.models.User
import com.marknkamau.globalgym.ui.editProfile.EditProfileActivity
import com.marknkamau.globalgym.ui.gymDetail.GymDetailsActivity
import com.marknkamau.globalgym.utils.onClick
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(), ProfileView {

    private lateinit var presenter: ProfilePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = ProfilePresenter(this, App.authService, App.userRepository, App.gymRepository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onResume() {
        super.onResume()

        presenter.getUser()
    }

    override fun onStop() {
        super.onStop()
        presenter.clearDisposables()
    }

    override fun displayNoInternetMessage() {
        Toast.makeText(requireContext(), R.string.no_internet, Toast.LENGTH_SHORT).show()
    }

    override fun displayDefaultErrorMessage() {
        Toast.makeText(requireContext(), R.string.an_error_has_occurred, Toast.LENGTH_SHORT).show()
    }

    override fun displayMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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

        btnEditProfile.setOnClickListener {
            val i = Intent(requireContext(), EditProfileActivity::class.java)
            i.putExtra(EditProfileActivity.USER_KEY, user)
            startActivity(i)
        }

    }

    override fun onGymRetrieved(gym: Gym) {
        tvPreferredGym.text = gym.name
        groupPreferredGym.visibility = View.VISIBLE
        groupPreferredGym.onClick {
            val intent = Intent(context, GymDetailsActivity::class.java)
            intent.putExtra(GymDetailsActivity.GYM_KEY, gym)
            startActivity(intent)
        }
    }
}
