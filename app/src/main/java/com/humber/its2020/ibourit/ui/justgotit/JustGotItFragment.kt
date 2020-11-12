package com.humber.its2020.ibourit.ui.justgotit

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.humber.its2020.ibourit.R
import com.humber.its2020.ibourit.constants.MapConstants
import kotlinx.android.synthetic.main.fragment_justgotit.*

class JustGotItFragment: Fragment() {
    private lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_justgotit, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title =
            " " + resources.getString(R.string.justgotit)

        map_view.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            setupMap()
        }
    }

    private fun setupMap() {
        map_view.getMapAsync { googleMap: GoogleMap ->
            map = googleMap
            val viewModel : JustGotItViewModel by viewModels()

            try {
                map.isMyLocationEnabled = true
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
            map.uiSettings.isMapToolbarEnabled = false
            map.uiSettings.isZoomControlsEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = true
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(MapConstants.INITIAL_LOCATION, 14.0f))

            // Add marker event handler
//            map.setOnInfoWindowClickListener { marker: Marker? ->
//                TODO()
//            }
//            map.setOnCameraIdleListener {
//                TODO()
//            }
//            map.setOnMarkerClickListener { marker: Marker? ->
//                TODO()
//            }
//            map.setOnMapClickListener { latLng: LatLng? ->
//                TODO()
//            }

            map_view.onResume()
        }
    }
}