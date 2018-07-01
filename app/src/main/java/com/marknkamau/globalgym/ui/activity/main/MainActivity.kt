package com.marknkamau.globalgym.ui.activity.main

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.marknkamau.globalgym.App
import com.marknkamau.globalgym.R
import com.marknkamau.globalgym.ui.activity.BaseActivity
import com.marknkamau.globalgym.ui.fragment.gyms.GymsMapFragment
import com.marknkamau.globalgym.ui.activity.login.LoginActivity
import com.marknkamau.globalgym.ui.fragment.profile.ProfileFragment
import com.marknkamau.globalgym.ui.activity.register.RegisterActivity
import com.marknkamau.globalgym.ui.activity.settings.SettingsActivity
import com.marknkamau.globalgym.ui.fragment.sessionsList.SessionsListFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), MainView {

    private lateinit var presenter: MainPresenter
    private val fragments = mutableListOf<Fragment>()

    private val SESSION_TAG = "session"
    private val GYMS_TAG = "gyms"
    private val PROFILE_TAG = "profile"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sessionsListFragment = SessionsListFragment()
        val gymsMapFragment = GymsMapFragment()
        val profileFragment = ProfileFragment()

        fragments.add(sessionsListFragment)
        fragments.add(gymsMapFragment)
        fragments.add(profileFragment)

        navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_session -> {
                    switchFragment(0, SESSION_TAG)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
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

    override fun onSignedInAndRegistered() {
        switchFragment(0, SESSION_TAG)
        navigation.visibility = View.VISIBLE
        pbLoading.visibility = View.GONE
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
