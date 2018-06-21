package com.marknkamau.globalgym.ui.gyms

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import com.marknkamau.globalgym.R
import com.marknkamau.globalgym.data.models.Cords
import com.marknkamau.globalgym.data.models.Gym
import com.marknkamau.globalgym.utils.RxUtils
import com.marknkamau.globalgym.utils.maps.LocationUtils
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class GymsMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var locationUtils: LocationUtils

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

    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        val gym = Gym("abc", "Pam Colding Gym", "http://via.placeholder.com/350x350", "0724",
                "www.pamcolding.com", "5:30AM", "9:00PM", "Kenya", "Nairobi", Cords(-1.297433, 36.793425))

//        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style))
        googleMap.isMyLocationEnabled = true
        googleMap.uiSettings.run {
            isMyLocationButtonEnabled = true
            isMapToolbarEnabled = false
        }

        val gymLoc = LatLng(gym.cords.lat, gym.cords.lng)
        val marker = googleMap.addMarker(MarkerOptions().position(gymLoc).title(gym.name))
        marker.tag = gym

        val infoWindowAdapter = GymInfoWindowAdapter(context!!)
        googleMap.setInfoWindowAdapter(infoWindowAdapter)
        googleMap.setOnInfoWindowClickListener {
            Toast.makeText(context, "Clicked!", Toast.LENGTH_SHORT).show()
        }

        locationUtils = LocationUtils(context!!)

        locationUtils.getCurrentLocation()
                .compose(RxUtils.applySingleSchedulers())
                .flatMap { location ->
                    locationUtils.reverseGeocode(location.latitude, location.longitude)
                }
                .subscribeBy(
                        onSuccess = {address ->
                            Timber.d("${address.latitude},${address.longitude}")
                            Timber.d(address.countryName)
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(address.latitude, address.longitude), 13f))
                        },
                        onError = { exception ->
                            Timber.e(exception)
                        }
                )
    }

}
