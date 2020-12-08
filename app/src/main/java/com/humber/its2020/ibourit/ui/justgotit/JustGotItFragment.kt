package com.humber.its2020.ibourit.ui.justgotit

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
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
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.humber.its2020.ibourit.R
import com.humber.its2020.ibourit.constants.MapConstants
import com.humber.its2020.ibourit.constants.MapConstants.Companion.REQUEST_CODE_PLACE
import com.humber.its2020.ibourit.ui.new_article.NewArticleFragment
import kotlinx.android.synthetic.main.fragment_justgotit.*
import kotlinx.android.synthetic.main.fragment_new_article.*
import java.util.*

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_PLACE) {
            if (resultCode == Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)

                val lat = place.latLng?.latitude!!
                val lng = place.latLng?.longitude!!
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 14.0f))

                val addresses = place.address!!.split(",")
                val address1 = addresses[0]
                val address2 = addresses[1]
                map_search.text = "$address1, $address2"
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                val status = Autocomplete.getStatusFromIntent(data!!)
                Log.i("address", status.statusMessage!!)
            }
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

        map_search.setOnClickListener {
            if (!Places.isInitialized()) {
                Places.initialize(requireContext(), getString(R.string.google_maps_key), Locale.CANADA)
            }

            val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(requireContext())
            startActivityForResult(intent, REQUEST_CODE_PLACE)
        }
    }
}