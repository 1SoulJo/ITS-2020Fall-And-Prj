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
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.humber.its2020.ibourit.R
import com.humber.its2020.ibourit.constants.MapConstants
import com.humber.its2020.ibourit.constants.MapConstants.REQUEST_CODE_PLACE
import com.humber.its2020.ibourit.entity.Article
import com.humber.its2020.ibourit.util.AddressUtil
import com.humber.its2020.ibourit.web.ApiClient
import kotlinx.android.synthetic.main.fragment_justgotit.*
import kotlinx.android.synthetic.main.map_info_popup.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
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

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
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

            try {
                map.isMyLocationEnabled = true
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
            map.uiSettings.isMapToolbarEnabled = false
            map.uiSettings.isZoomControlsEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = true
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(MapConstants.INITIAL_LOCATION, 14.0f))

            // display markers
            map.setOnCameraIdleListener {
                val address = AddressUtil.coordinateToAddress(
                    requireContext(),
                    map.cameraPosition.target.latitude,
                    map.cameraPosition.target.longitude
                )
                ApiClient.getArticlesByAddress(
                    address.locality, address.adminArea, address.countryName,
                    object : Callback<List<Article>> {
                        override fun onResponse(
                            call: Call<List<Article>>,
                            response: Response<List<Article>>) {
                            if (!response.body().isNullOrEmpty()) {
                                val list = response.body()!!
                                for (a in list) {
                                    Log.d("map", a.brand)
                                    val position = LatLng(a.lat, a.lng)
                                    googleMap.addMarker(
                                        MarkerOptions().position(position).title(
                                            "${a.brand};${a.name};${a.userName};${a.date}"))
                                }
                            }
                        }

                        override fun onFailure(call: Call<List<Article>>, t: Throwable) {
                        }
                    })
            }

            // marker info view
            map.setInfoWindowAdapter(object: GoogleMap.InfoWindowAdapter {
                override fun getInfoWindow(p0: Marker?): View? {
                    return null
                }

                override fun getInfoContents(p0: Marker?): View {
                    val v =
                        requireActivity().layoutInflater.inflate(R.layout.map_info_popup, null)
                    val data = p0!!.title.split(";")
                    v.brand.text = data[0]
                    v.name.text = data[1]
                    v.user.text = data[2]
                    v.date.text = data[3].substring(0, 10)

                    return v
                }
            })

            map_view.onResume()
        }

        map_search.setOnClickListener {
            if (!Places.isInitialized()) {
                Places.initialize(requireContext(),
                    getString(R.string.google_maps_key), Locale.CANADA)
            }

            val fields = listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS
            )

            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(requireContext())
            startActivityForResult(intent, REQUEST_CODE_PLACE)
        }
    }
}