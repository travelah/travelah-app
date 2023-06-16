package com.travelah.travelahapp.view.maps

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.travelah.travelahapp.R
import com.travelah.travelahapp.data.remote.models.Places
import com.travelah.travelahapp.databinding.ActivityMapsBinding
import com.travelah.travelahapp.view.post.AddEditPostActivity
import java.io.IOException


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private var currLat: Double? = null
    private var currLong: Double? = null
    private var currAddress: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapsInitializer.initialize(this, MapsInitializer.Renderer.LATEST) {}

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val searchView = binding.idSearchView

        val place = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_PLACES, Places::class.java)
        } else {
            @Suppress("deprecation")
            intent.getParcelableExtra(EXTRA_PLACES)
        }

        if (place != null) {
            searchView.visibility = View.GONE
            supportActionBar?.hide()
        } else {
            searchView.visibility = View.VISIBLE
        }

        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // on below line we are getting the
                // location name from search view.
                val location = searchView.query.toString()

                // below line is to create a list of address
                // where we will store the list of all address.
                var addressList: List<Address>? = null

                // checking if the entered location is null or not.
                // checking if the entered location is null or not.
                if (location != null || location == "") {
                    // on below line we are creating and initializing a geo coder.
                    val geocoder = Geocoder(this@MapsActivity)
                    try {
                        // on below line we are getting location from the
                        // location name and adding that location to address list.
                        addressList = geocoder.getFromLocationName(location, 1)
                        // on below line we are getting the location
                        // from our list a first position.
                        if (addressList != null) {
                            if (addressList.isNotEmpty()) {
                                val address = addressList!![0]
                                val fullAddress = address.getAddressLine(0)
                                // on below line we are creating a variable for our location
                                // where we will add our locations latitude and longitude.
                                val latLng = LatLng(address.latitude, address.longitude)
                                currLat = address.latitude
                                currLong = address.longitude
                                currAddress = fullAddress

                                // on below line we are adding marker to that position.
                                mMap.addMarker(MarkerOptions().position(latLng).title(location))

                                // below line is to animate camera to that position.
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                            }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        val place = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_PLACES, Places::class.java)
        } else {
            @Suppress("deprecation")
            intent.getParcelableExtra(EXTRA_PLACES)
        }

        if (place != null) {
            currLat = place.lat
            currLong = place.lng
            val placeLoc = LatLng(place.lat, place.lng)
            mMap.addMarker(
                MarkerOptions()
                    .position(placeLoc)
                    .title(place.place)
            )
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(placeLoc, 15f))
        }

//        mMap.setOnPoiClickListener { pointOfInterest ->
//            val poiMarker = mMap.addMarker(
//                MarkerOptions()
//                    .position(pointOfInterest.latLng)
//                    .title(pointOfInterest.name)
//                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
//            )
//            poiMarker?.showInfoWindow()
//        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_add_edit_post, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add_edit -> {
                if (currLat != null && currLong != null) {
                    val intent = Intent()
                    intent.putExtra("lat", currLat)
                    intent.putExtra("long", currLong)
                    intent.putExtra("address", currAddress)

                    setResult(AddEditPostActivity.MAPS_RESULT, intent)
                    finish()
                } else {
                    Toast.makeText(
                        this@MapsActivity,
                        getString(R.string.choose_location_first),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                true
            }
            else -> true
        }
    }

    companion object {
        const val EXTRA_PLACES = "extra_places"
    }
}