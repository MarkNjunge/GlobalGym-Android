package com.marknkamau.globalgym.ui.editProfile

import android.os.Bundle
import android.widget.Toast
import com.marknkamau.globalgym.App
import com.marknkamau.globalgym.R
import com.marknkamau.globalgym.data.models.User
import com.marknkamau.globalgym.ui.base.BaseActivity
import com.marknkamau.globalgym.ui.userDetails.UserDetailsFragment
import com.marknkamau.globalgym.utils.RxUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class EditProfileActivity : BaseActivity() {

    companion object {
        const val USER_KEY = "user"
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        supportActionBar?.title = getString(R.string.edit_profile)

        val user = intent.getParcelableExtra<User>(USER_KEY)
        val bundle = Bundle()
        bundle.putParcelable(USER_KEY, user)

        val userDetailsFragment = UserDetailsFragment()
        userDetailsFragment.arguments = bundle
        userDetailsFragment.onComplete = {
            val (firstName, lastName, phone, year, country, gender, weight, targetWeight) = it
            val newUser = User(user.userId, firstName, lastName, user.email, phone, user.profilePhoto, year, country, gender, weight, targetWeight, user.preferredGym)

            val disposable = App.userRepository.updateUser(newUser)
                    .compose(RxUtils.applySingleSchedulers())
                    .subscribeBy(
                            onSuccess = {
                                finish()
                                Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                            },
                            onError = {
                                Timber.e(it)
                                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show();
                            }
                    )

            compositeDisposable.add(disposable)
        }

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_holder, userDetailsFragment, "user_details")
                .commit()
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }
}
