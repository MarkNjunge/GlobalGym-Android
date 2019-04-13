package com.marknkamau.globalgym.ui.gymDetail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.transition.TransitionManager
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.marknkamau.globalgym.App
import com.marknkamau.globalgym.R
import com.marknkamau.globalgym.data.models.Gym
import com.marknkamau.globalgym.data.models.Instructor
import com.marknkamau.globalgym.ui.base.BaseActivity
import com.marknkamau.globalgym.utils.GlideApp
import com.marknkamau.globalgym.utils.RxUtils
import com.marknkamau.globalgym.utils.maps.LocationUtils
import com.uber.sdk.android.rides.RideParameters
import com.uber.sdk.android.rides.RideRequestButtonCallback
import com.uber.sdk.rides.client.error.ApiError
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_gym_details.*
import timber.log.Timber

class GymDetailsActivity : BaseActivity(), GymDetailView {
    companion object {
        const val GYM_KEY = "gym_key"
    }

    private lateinit var presenter: GymDetailPresenter
    private lateinit var adapter: InstructorsAdapter

    @SuppressLint("SetTextI18n", "CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gym_details)

        val gym = intent.extras.get(GYM_KEY) as Gym

        supportActionBar?.run {
            title = gym.name
            setDisplayHomeAsUpEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        presenter = GymDetailPresenter(this, gym, App.userRepository, App.gymRepository)

        tvGymTitle.text = gym.name
        tvOpenTime.text = gym.openTime
        tvCloseTime.text = gym.closeTime
        tvPhone.text = gym.phone
        tvWebsite.text = gym.website
        tvLocation.text = "${gym.city},${gym.country}"

        rvInstructors.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        adapter = InstructorsAdapter(this) {
            // TODO: Add ability to contact instructor
        }
        rvInstructors.adapter = adapter
        presenter.getInstructors(gym.instructors)

        layoutCall.setOnClickListener {
            val i = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${gym.phone}"))
            startActivity(i)
        }

        layoutWebsite.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(gym.website)))
        }

        layoutDirections.setOnClickListener {
            val gmmIntentUri = Uri.parse("geo:${gym.cords.lat},${gym.cords.lng}?q=${gym.cords.lat},${gym.cords.lng}(${gym.name})")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.`package` = "com.google.android.apps.maps"
            if (mapIntent.resolveActivity(packageManager) != null) {
                startActivity(mapIntent)
            }
        }

        layoutPreferred.setOnClickListener {
            presenter.setGymAsPreferred()
        }

        GlideApp.with(this)
                .load(gym.images.first())
                .into(imgHeader)

        GlideApp.with(this)
                .load(gym.logo)
                .into(imgLogo)

        val locationUtils = LocationUtils(this)

        locationUtils.reverseGeocode(gym.cords.lat, gym.cords.lng)
                .compose(RxUtils.applySingleSchedulers())
                .subscribeBy(
                        onSuccess = { address ->
                            tvAddress.text = address.getAddressLine(0)
                            tvAddress.visibility = View.VISIBLE
                        },
                        onError = {
                            Timber.e(it)
                            Toast.makeText(this, "Unable to get street address", Toast.LENGTH_SHORT).show()
                        }
                )

        locationUtils.getCurrentLocation()
                .subscribeBy(
                        onSuccess = { location ->
                            TransitionManager.beginDelayedTransition(linearLayout5)
                            val rideParameters = RideParameters.Builder()
                                    .setDropoffLocation(gym.cords.lat, gym.cords.lng, gym.name, null)
                                    .setPickupLocation(location.latitude, location.longitude, "My location", null)
                                    .build()
                            btnUber.setRideParameters(rideParameters)
                            btnUber.setSession(App.uberSession)
                            btnUber.visibility = View.VISIBLE

                            val value: RideRequestButtonCallback = object : RideRequestButtonCallback {
                                override fun onRideInformationLoaded() {}

                                override fun onError(apiError: ApiError?) {
                                    Toast.makeText(this@GymDetailsActivity, "Error getting Uber estimate", Toast.LENGTH_SHORT).show()
                                    apiError?.let { error ->
                                        error.clientErrors.forEach { clientError ->
                                            Timber.e("${clientError.code}, ${clientError.status}, ${clientError.title}")
                                        }
                                    }
                                }

                                override fun onError(throwable: Throwable?) {
                                    Toast.makeText(this@GymDetailsActivity, "Error getting Uber estimate", Toast.LENGTH_SHORT).show()
                                    throwable?.let { Timber.e(throwable) }
                                }

                            }

                            btnUber.setCallback(value)
                            btnUber.loadRideInformation()
                        },
                        onError = {
                            Timber.e(it)
                        }
                )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        super.onStop()
        presenter.clearDisposables()
    }

    override fun displayNoInternetMessage() {
        Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show()
    }

    override fun displayDefaultErrorMessage() {
        Toast.makeText(this, R.string.an_error_has_occurred, Toast.LENGTH_SHORT).show()
    }

    override fun displayMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onInstructorsReceived(instructors: List<Instructor>) {
        adapter.setItems(instructors)
        Timber.d(adapter.itemCount.toString())
    }

    override fun onGymIsPreferred() {
        imgPreferred.setImageResource(R.drawable.ic_favorite)
    }
}
