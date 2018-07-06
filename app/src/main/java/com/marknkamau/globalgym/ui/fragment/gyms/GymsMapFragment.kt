package com.marknkamau.globalgym.ui.fragment.gyms

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.marknkamau.globalgym.App

import com.marknkamau.globalgym.R
import com.marknkamau.globalgym.data.models.Gym
import com.marknkamau.globalgym.ui.activity.gymdetail.GymDetailsActivity
import com.marknkamau.globalgym.utils.RxSearch
import com.marknkamau.globalgym.utils.RxUtils
import com.marknkamau.globalgym.utils.maps.LocationUtils
import com.marknkamau.globalgym.utils.maps.MapUtils
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_gyms_map.*
import timber.log.Timber
import java.util.concurrent.TimeUnit

class GymsMapFragment : Fragment(), OnMapReadyCallback, GymsView {
    private lateinit var locationUtils: LocationUtils
    private lateinit var mapUtils: MapUtils
    private lateinit var presenter: GymsPresenter
    private lateinit var gymSearchAdapter: GymSearchAdapter

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

        gymSearchAdapter = GymSearchAdapter {
            val intent = Intent(requireContext(), GymDetailsActivity::class.java)
            intent.putExtra(GymDetailsActivity.GYM_KEY, it)
            startActivity(intent)
        }

        rvGymResults.layoutManager = LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)
        rvGymResults.adapter = gymSearchAdapter

        presenter = GymsPresenter(this, App.dataRepository)

    }

    override fun onStop() {
        super.onStop()
        presenter.clearDisposables()
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
//        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style))
//        googleMap.isMyLocationEnabled = true
        googleMap.uiSettings.run {
            isMyLocationButtonEnabled = true
            isMapToolbarEnabled = false
        }

        locationUtils = LocationUtils(context!!)
        mapUtils = MapUtils(requireContext(), googleMap)

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
                .subscribeBy(
                        onSuccess = { location ->
                            Timber.d("${location.latitude},${location.longitude}")
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 13f))
                            presenter.getGyms(location.latitude, location.longitude)
                        },
                        onError = { exception ->
                            Timber.e(exception)
                        }
                )

        // Initialize gym search
        RxSearch.fromEditText(etGymName)
                .doOnNext {
                    if (it.isEmpty())
                        rvGymResults.visibility = View.GONE
                }
                .filter { it.length > 3 }
                .debounce(300, TimeUnit.MILLISECONDS)
                .subscribeBy {

                    presenter.searchForGym(it)
                }
    }

    override fun displayMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onGymsRetrieved(gyms: List<Gym>) {
        pbLoading.visibility = View.GONE
        gyms.forEach { gym ->
            mapUtils.addGymMarker(gym)
        }
    }

    override fun showSearchLoading() {
        requireActivity().runOnUiThread {
            pbGymSearch.visibility = View.VISIBLE
        }
    }

    override fun hideSearchLoading() {
        requireActivity().runOnUiThread {
            pbGymSearch.visibility = View.GONE
        }
    }

    override fun onGymSearchResultRetrieved(gyms: List<Gym>) {
        gymSearchAdapter.setItems(gyms)
        rvGymResults.visibility = View.VISIBLE
    }

}
