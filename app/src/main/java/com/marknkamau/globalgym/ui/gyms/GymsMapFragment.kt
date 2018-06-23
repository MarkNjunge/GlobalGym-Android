package com.marknkamau.globalgym.ui.gyms

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.marknkamau.globalgym.App

import com.marknkamau.globalgym.R
import com.marknkamau.globalgym.data.models.Gym
import com.marknkamau.globalgym.ui.gymdetail.GymDetailsActivity
import com.marknkamau.globalgym.utils.RxUtils
import com.marknkamau.globalgym.utils.maps.LocationUtils
import com.marknkamau.globalgym.utils.maps.MapUtils
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class GymsMapFragment : Fragment(), OnMapReadyCallback, GymsView {
    private lateinit var locationUtils: LocationUtils
    private lateinit var mapUtils: MapUtils

    private lateinit var presenter: GymsPresenter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_gyms_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rxPermissions = RxPermissions(activity!!)
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe { granted: Boolean ->
                    if (granted) {
                        val fragment: SupportMapFragment = childFragmentManager.findFragmentById(R.id.mapGyms) as SupportMapFragment
                        fragment.getMapAsync(this)
                    } else {
                        Toast.makeText(context, "Location permission is required", Toast.LENGTH_SHORT).show()
                    }
                }

        presenter = GymsPresenter(this, App.apiService)

    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
//        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style))
        googleMap.isMyLocationEnabled = true
        googleMap.uiSettings.run {
            isMyLocationButtonEnabled = true
            isMapToolbarEnabled = false
        }

        locationUtils = LocationUtils(context!!)
        mapUtils = MapUtils(googleMap)

        val infoWindowAdapter = GymInfoWindowAdapter(context!!)
        googleMap.setInfoWindowAdapter(infoWindowAdapter)
        googleMap.setOnInfoWindowClickListener { marker ->
            val gym = marker.tag as Gym
            val intent = Intent(context, GymDetailsActivity::class.java)
            intent.putExtra(GymDetailsActivity.GYM_KEY, gym)
            startActivity(intent)
        }

        locationUtils.getCurrentLocation()
                .compose(RxUtils.applySingleSchedulers())
                .flatMap { location ->
                    locationUtils.reverseGeocode(location.latitude, location.longitude)
                }
                .subscribeBy(
                        onSuccess = { address ->
                            Timber.d("${address.latitude},${address.longitude}")
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(address.latitude, address.longitude), 13f))
                            presenter.getGyms(address.latitude, address.longitude, address.countryName)
                        },
                        onError = { exception ->
                            Timber.e(exception)
                        }
                )
    }

    override fun displayMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onGymsRetrieved(gyms: List<Gym>) {
        gyms.forEach { gym ->
            mapUtils.addGymMarker(gym)
        }
    }

}
