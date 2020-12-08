package com.humber.its2020.ibourit.ui.new_article

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.loader.content.CursorLoader
import com.bumptech.glide.request.RequestOptions
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.model.Image
import com.glide.slider.library.SliderLayout
import com.glide.slider.library.slidertypes.DefaultSliderView
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place.Field
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.humber.its2020.ibourit.R
import com.humber.its2020.ibourit.constants.Category
import com.humber.its2020.ibourit.constants.MapConstants.Companion.REQUEST_CODE_PLACE
import com.humber.its2020.ibourit.credential.Credential
import com.humber.its2020.ibourit.entity.Article
import com.humber.its2020.ibourit.entity.ItemInfo
import com.humber.its2020.ibourit.web.ApiClient
import kotlinx.android.synthetic.main.fragment_new_article.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class NewArticleFragment : Fragment() {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_article, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)

        // title
        (activity as AppCompatActivity).supportActionBar?.title =
            " " + resources.getString(R.string.post_new_article)

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
            Places.initialize(requireContext(), getString(R.string.google_maps_key), Locale.CANADA)
        }
        placesClient = Places.createClient(requireContext())

        updateCurrentLocation()

        location_layout.setOnClickListener {
            val fields = listOf(Field.ID, Field.NAME, Field.LAT_LNG, Field.ADDRESS)
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(requireContext())
            startActivityForResult(intent, REQUEST_CODE_PLACE)
        }

        // item info setup
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.categories,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            item_category.adapter = adapter
        }
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
                val sliderView = DefaultSliderView(requireContext())

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

        if (requestCode == REQUEST_CODE_PLACE) {
            if (resultCode == Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                latLng = place.latLng!!
                val lat = place.latLng?.latitude
                val lng = place.latLng?.longitude
                Log.d("NewArticle", "${place.address} $lat , $lng")

                val addresses = place.address!!.split(",").reversed()
                val city = addresses[2]
                val country = addresses[0]
                text_location.visibility = View.VISIBLE
                text_location.text = "$city, $country"
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                val status = Autocomplete.getStatusFromIntent(data!!)
                Log.i("address", status.statusMessage!!)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // post article to server
        if (item.itemId == R.id.menu_post) {
            val userId = Credential.id()
            val pattern = "yyyy-MM-dd'T'HH:mm:ss"
            val simpleDateFormat = SimpleDateFormat(pattern, Locale.CANADA)
            val date: String = simpleDateFormat.format(Date())
            val categoryInt = Category.byName(item_category.selectedItem.toString()).ordinal
            val article = Article(
                generateArticleId(userId), userId, Credential.name(),
                item_description.text.toString(), 0, ArrayList<String>(), date,
                categoryInt,
                act_brand.text.toString(),
                item_name.text.toString(),
                Integer.parseInt(item_price.text.toString()), ArrayList<String>(),
                lat = latLng.latitude, lng = latLng.longitude)

            // upload article
            ApiClient().uploadArticle(article, object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    Log.d("NewArticle", "upload success")
                    // upload images
                    for ((order, image) in images.withIndex()) {
                        val file = File(getRealPathFromURI(image.uri)!!)
                        val filePart = MultipartBody.Part.createFormData(
                            "file",
                            file.name,
                            RequestBody.create(MediaType.parse("image/*"), file)
                        )

                        Log.d("NewArticle", article.articleId)
                        ApiClient().uploadImage(userId, categoryInt, article.articleId, filePart, order,
                            object : Callback<Void> {
                                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                    Log.d("NewArticle", "Image upload success")
                                }

                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                    Log.d("NewArticle", t.message!!)
                                }
                            })
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("NewArticle", "upload fail ${t.message}")
                }
            })
        }

        return super.onOptionsItemSelected(item)
    }

    private fun generateArticleId(userId: String) : String {
        val date = Date()
        val unique = "$userId$date"
        return Credential.hashString("SHA-1", unique)
    }

    private fun getRealPathFromURI(contentUri: Uri): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(requireContext(), contentUri, proj, null, null, null)
        val cursor: Cursor = loader.loadInBackground()!!
        val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val result: String = cursor.getString(columnIndex)
        cursor.close()

        return result
    }

    private fun verifyStoragePermissions() {
        val permission = ActivityCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.new_article, menu)
    }

    private fun updateCurrentLocation() {
        val placeFields: List<Field> = listOf(Field.NAME, Field.ADDRESS, Field.LAT_LNG)
        val request: FindCurrentPlaceRequest = FindCurrentPlaceRequest.newInstance(placeFields)

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) {

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
                        Log.e("NewArticle", "Place not found: ${exception.statusCode}")
                    }
                }
            }
        } else {
            // ToDo: Request permission if necessary
        }
    }
}