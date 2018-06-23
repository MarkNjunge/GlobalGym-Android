package com.marknkamau.globalgym.ui.gymdetail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.marknkamau.globalgym.App
import com.marknkamau.globalgym.R
import com.marknkamau.globalgym.data.models.Gym
import com.marknkamau.globalgym.data.models.Instructor
import com.marknkamau.globalgym.utils.GlideApp
import com.marknkamau.globalgym.utils.RxUtils
import com.marknkamau.globalgym.utils.maps.LocationUtils
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_gym_details.*
import timber.log.Timber

class GymDetailsActivity : AppCompatActivity(), GymDetailView {
    companion object {
        const val GYM_KEY = "gym_key"
    }

    private lateinit var presenter: GymDetailPresenter
    private lateinit var adapter: InstructorsAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gym_details)

        presenter = GymDetailPresenter(this, App.apiService)

        val gym = intent.extras.get(GYM_KEY) as Gym

        tvGymTitle.text = gym.name
        tvOpenTime.text = gym.openTime
        tvCloseTime.text = gym.closeTime
        tvPhone.text = gym.phone
        tvWebsite.text = gym.website
        tvLocation.text = "${gym.city},${gym.country}"

        rvInstructors.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        adapter = InstructorsAdapter(this) { }
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
            //            val gmmIntentUri = Uri.parse("geo:${gym.cords.lat},${gym.cords.lng}")
            val gmmIntentUri = Uri.parse("google.navigation:q=${gym.cords.lat},${gym.cords.lng}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.`package` = "com.google.android.apps.maps"
            if (mapIntent.resolveActivity(packageManager) != null) {
                startActivity(mapIntent)
            }
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
    }

    override fun displayMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    override fun onInstructorsReceived(instructors: List<Instructor>) {
        adapter.setItems(instructors)
        Timber.d(adapter.itemCount.toString())
    }
}
