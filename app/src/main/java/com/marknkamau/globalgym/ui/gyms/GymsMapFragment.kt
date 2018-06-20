package com.marknkamau.globalgym.ui.gyms

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

import com.marknkamau.globalgym.R
import com.marknkamau.globalgym.data.models.Cords
import com.marknkamau.globalgym.data.models.Gym
import com.tbruyelle.rxpermissions2.RxPermissions

class GymsMapFragment : Fragment(), OnMapReadyCallback {

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

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style))

        val sydney = LatLng(-34.0, 151.0)
        val marker = googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        val gym = Gym("abc", "Pam Colding Gym", "http://via.placeholder.com/350x350", "0724",
                "www.pamcolding.com", "5:30AM", "9:00PM", "Kenya", "Nairobi", Cords(1L, 1L))

        val infoWindowAdapter = GymInfoWindowAdapter(context!!)
        googleMap.setOnInfoWindowClickListener {
            Toast.makeText(context, "Clicked!", Toast.LENGTH_SHORT).show();
        }
        googleMap.setInfoWindowAdapter(infoWindowAdapter)

        marker.tag = gym
        marker.showInfoWindow()
    }

}
