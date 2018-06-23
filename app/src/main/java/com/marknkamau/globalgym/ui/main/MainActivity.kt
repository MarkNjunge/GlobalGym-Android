package com.marknkamau.globalgym.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.marknkamau.globalgym.App
import com.marknkamau.globalgym.R
import com.marknkamau.globalgym.ui.gyms.GymsMapFragment
import com.marknkamau.globalgym.ui.login.LoginActivity
import com.marknkamau.globalgym.ui.profile.ProfileFragment
import com.marknkamau.globalgym.ui.register.RegisterActivity
import com.marknkamau.globalgym.ui.workouts.WorkoutFragment
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity(), MainView {

    private lateinit var presenter: MainPresenter
    private val fragments = mutableListOf<Fragment>()

    private val WORKOUT_TAG = "workout"
    private val GYMS_TAG = "gyms"
    private val PROFILE_TAG = "profile"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val workoutFragment = WorkoutFragment()
        val gymsMapFragment = GymsMapFragment()
        val profileFragment = ProfileFragment()

        fragments.add(workoutFragment)
        fragments.add(gymsMapFragment)
        fragments.add(profileFragment)

        switchFragment(0, WORKOUT_TAG)

        navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_workouts -> {
                    switchFragment(0, WORKOUT_TAG)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_gyms -> {
                    switchFragment(1, GYMS_TAG)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    switchFragment(2, PROFILE_TAG)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

        presenter = MainPresenter(this, App.authService, App.apiService, App.paperService)
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

    private fun switchFragment(pos: Int, tag: String) {
        if (supportFragmentManager.findFragmentByTag(tag) == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_holder, fragments[pos], tag)
                    .commit()
        }
    }
}
