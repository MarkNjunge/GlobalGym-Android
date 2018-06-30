package com.marknkamau.globalgym.ui.activity.selectGym

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.marknkamau.globalgym.App
import com.marknkamau.globalgym.R
import com.marknkamau.globalgym.data.models.Gym
import com.marknkamau.globalgym.data.remote.ApiService
import com.marknkamau.globalgym.ui.activity.BaseActivity
import com.marknkamau.globalgym.ui.fragment.gyms.GymInfoWindowAdapter
import com.marknkamau.globalgym.utils.RxUtils
import com.marknkamau.globalgym.utils.maps.LocationUtils
import com.marknkamau.globalgym.utils.maps.MapUtils
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_select_gym.*
import timber.log.Timber

class SelectGymActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var locationUtils: LocationUtils
    private lateinit var mapUtils: MapUtils
    private lateinit var apiService: ApiService

    companion object {
        const val SELECTED_GYM = "selected_gym"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_gym)

        supportActionBar?.run {
            title = getString(R.string.select_gym)
        }

        apiService = App.apiService

        val rxPermissions = RxPermissions(this)
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe { granted: Boolean ->
                    if (granted) {
                        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapGyms) as SupportMapFragment
                        mapFragment.getMapAsync(this)
                    } else {
                        Toast.makeText(this, "Location permission is required", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.isMyLocationEnabled = true
        googleMap.uiSettings.run {
            isMyLocationButtonEnabled = true
            isMapToolbarEnabled = false
        }

        locationUtils = LocationUtils(this)
        mapUtils = MapUtils(this, googleMap)

        val infoWindowAdapter = GymInfoWindowAdapter(this)
        googleMap.setInfoWindowAdapter(infoWindowAdapter)
        googleMap.setOnInfoWindowClickListener { marker ->
            val gym = marker.tag as Gym
            val i = Intent()
            i.putExtra(SELECTED_GYM, gym)
            setResult(Activity.RESULT_OK, i)
            finish()
        }

        // Get current location
        // Reverse geocode to get the country
        // Get nearby gyms
        locationUtils.getCurrentLocation()
                .flatMap { location ->
                    locationUtils.reverseGeocode(location.latitude, location.longitude)
                }
                .flatMap { address ->
                    Timber.d("${address.latitude},${address.longitude}")
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(address.latitude, address.longitude), 13f))

                    apiService.getNearbyGyms(address.countryName, address.latitude, address.longitude, 50 * 1000)
                            .compose(RxUtils.applySingleSchedulers())
                }
                .subscribeBy(
                        onSuccess = { gyms ->
                            pbLoading.visibility = View.GONE
                            gyms.forEach { gym ->
                                mapUtils.addGymMarker(gym)
                            }
                        },
                        onError = { exception ->
                            Timber.e(exception)
                            Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show();
                            pbLoading.visibility = View.GONE
                        }
                )

    }
}
