package com.humber.its2020.ibourit.ui.inventory

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.loader.content.CursorLoader
import com.bumptech.glide.request.RequestOptions
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.model.Image
import com.glide.slider.library.SliderLayout
import com.glide.slider.library.slidertypes.DefaultSliderView
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.humber.its2020.ibourit.R
import com.humber.its2020.ibourit.constants.Category
import com.humber.its2020.ibourit.constants.MapConstants
import com.humber.its2020.ibourit.database.AppDatabase
import com.humber.its2020.ibourit.entity.InventoryItem
import com.humber.its2020.ibourit.entity.ItemImage
import kotlinx.android.synthetic.main.activity_add_inventory.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class AddInventoryActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_EXTERNAL_STORAGE = 1
        private val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    lateinit var images : ArrayList<Image>
    private lateinit var placesClient : PlacesClient
    private lateinit var latLng: LatLng
    private lateinit var category: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_inventory)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayUseLogoEnabled(true)

        category = intent.getStringExtra("category")!!

        supportActionBar?.title = " Add new inventory"
        supportActionBar?.setLogo(R.drawable.ic_bag_09)

        item_category.text = category

        // add image button
        image_area.setOnClickListener {
            verifyStoragePermissions()
            val i = ImagePicker.create(this)
            i.start()
        }
        add_image_sm.setOnClickListener {
            val i = ImagePicker.create(this)
            if (this::images.isInitialized) {
                i.origin(images)
            }
            i.start()
        }

        // location setup
        if (!Places.isInitialized()) {
            Places.initialize(this, getString(R.string.google_maps_key), Locale.CANADA)
        }
        placesClient = Places.createClient(this)

        updateCurrentLocation()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            add_image.visibility = View.GONE
            add_image_sm.visibility = View.VISIBLE
            slider.visibility = View.VISIBLE

            val requestOptions = RequestOptions()
            requestOptions.centerCrop()

            slider.removeAllSliders()
            images = ImagePicker.getImages(data) as ArrayList<Image>
            for (image in images) {
                Log.d(this::class.simpleName, image.uri.toString())
                val sliderView = DefaultSliderView(this)

                sliderView
                    .image(File(getRealPathFromURI(image.uri)!!))
                    .setRequestOption(requestOptions)
                    .setProgressBarVisible(true)

                sliderView.bundle(Bundle())
                slider.addSlider(sliderView)
            }
            slider.setPresetTransformer(SliderLayout.Transformer.Default)

            slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)
            slider.stopAutoCycle()
            slider.currentPosition = 0
        }

        if (requestCode == MapConstants.REQUEST_CODE_PLACE) {
            if (resultCode == Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                latLng = place.latLng!!
                val lat = place.latLng?.latitude
                val lng = place.latLng?.longitude

                val addresses = place.address!!.split(",").reversed()
                val city = addresses[2]
                val country = addresses[0]
                text_location.visibility = View.VISIBLE
                text_location.text = "$city, $country"
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                val status = Autocomplete.getStatusFromIntent(data!!)
                Log.i("AddInventoryActivity", status.statusMessage!!)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_inventory, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_save_new_inventory) {
            try {
                val newItem = InventoryItem(
                    id = 0,
                    brand = item_brand.text.toString(),
                    category = Category.byName(this.category).ordinal,
                    name = item_model.text.toString(),
                    price = Integer.parseInt(item_price.text.toString()),
                    date = Date().time
                )

                val itemDao = AppDatabase.get(this).itemDao()
                val imageDao = AppDatabase.get(this).itemImageDao()
                GlobalScope.launch {
                    val newId = itemDao.save(newItem)
                    for (image in images) {
                        imageDao.save(
                            ItemImage(
                                id = 0,
                                itemId = newId.toInt(),
                                uri = image.uri.toString()
                            )
                        )
                    }
                }

                finish()
            } catch (e: Exception) {
                finish()
                return true
            }

        }

        return super.onOptionsItemSelected(item)
    }

    private fun verifyStoragePermissions() {
        val permission = ActivityCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE)
        }
    }

    private fun getRealPathFromURI(contentUri: Uri): String {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(this, contentUri, proj, null, null, null)
        val cursor: Cursor = loader.loadInBackground()!!
        val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val result: String = cursor.getString(columnIndex)
        cursor.close()

        return result
    }

    private fun updateCurrentLocation() {
        val placeFields: List<Place.Field> = listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
        val request: FindCurrentPlaceRequest = FindCurrentPlaceRequest.newInstance(placeFields)

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {

            val placeResponse = placesClient.findCurrentPlace(request)
            placeResponse.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val placeLikelihood = task.result?.placeLikelihoods!![0]
                    latLng = placeLikelihood.place.latLng!!
                    val addresses = placeLikelihood.place.address!!.split(",").reversed()
                    val city = addresses[2]
                    val country = addresses[0]

                    if (text_location != null) {
                        text_location.visibility = View.VISIBLE
                        text_location.text = "$city, $country"
                    }
                } else {
                    val exception = task.exception
                    if (exception is ApiException) {
                        Log.e("AddInventoryActivity", "Place not found: ${exception.statusCode}")
                    }
                }
            }
        } else {
            // ToDo: Request permission if necessary
        }
    }
}