package com.marknkamau.globalgym.ui.fragment.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.constraint.Group
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.marknkamau.globalgym.App

import com.marknkamau.globalgym.R
import com.marknkamau.globalgym.data.models.Gym
import com.marknkamau.globalgym.data.models.User
import com.marknkamau.globalgym.ui.activity.gymdetail.GymDetailsActivity
import com.marknkamau.globalgym.ui.activity.login.LoginActivity
import com.marknkamau.globalgym.utils.GlideApp
import com.marknkamau.globalgym.utils.onClick
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(), ProfileView {

    private lateinit var presenter: ProfilePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = ProfilePresenter(this, App.authService, App.paperService, App.apiService)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onResume() {
        super.onResume()

        presenter.getUser()
    }

    override fun onPause() {
        super.onPause()

        presenter.dispose()
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

        GlideApp.with(this@ProfileFragment)
                .load(user.profilePhoto)
                .circleCrop()
                .into(imgProfileIcon)

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
