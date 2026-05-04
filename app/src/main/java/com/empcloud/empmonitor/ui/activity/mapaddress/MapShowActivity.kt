package com.empcloud.empmonitor.ui.activity.mapaddress

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.databinding.ActivityMapShowBinding
import com.empcloud.empmonitor.ui.adapters.SearchResultsAdapter
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import com.empcloud.empmonitor.utils.NativeLib
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint

class MapShowActivity : AppCompatActivity(), OnMapReadyCallback {

    private var lat: Double? = null
    private var long:Double? = null
    private lateinit var binding:ActivityMapShowBinding
    private lateinit var map: GoogleMap
    private lateinit var currentMarker: Marker
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private  var city:String? = null
    private  var country:String? = null
    private  var addressText:String? = null
    private  var zip: String? = null
    private  var state:String? = null
    private lateinit var searchResults: RecyclerView
    private lateinit var searchBar: EditText
    private lateinit var searchResultsAdapter: SearchResultsAdapter
    private lateinit var placesClient: PlacesClient
//    private lateinit var marker: Marker
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapShowBinding.inflate(layoutInflater)
        setContentView(binding.root)


        Places.initialize(applicationContext,NativeLib().getGoogleMapApiKey())
        placesClient = Places.createClient(this)

        searchBar = binding.searchBar
        searchResults = binding.searchResults
        searchResults.layoutManager = LinearLayoutManager(this)
        searchResultsAdapter = SearchResultsAdapter(placesClient){ placeId ->
//            fetchPlaceDetails(placeId)
            fetchPlaceDetails(placeId)
        }

        if(searchBar.text.isEmpty()){
            searchResults.visibility = View.GONE
        }

        searchResults.adapter = searchResultsAdapter

        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // No action needed
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.length > 3) {
//                    searchPlaces(s.toString(), currentLatLng)
                    getCurrentLocationAndSearch(s.toString())
                    disableMapGestures()
                } else {
                    searchResults.visibility = View.GONE
                }
            }
        })

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        binding.currentlocbtn.setOnClickListener {
            binding.searchResults.visibility = View.GONE
            zoomToCurrentLocation()
        }

        binding.backbtn.setOnClickListener {

            val intent = Intent(applicationContext,MapAddressActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.bottomSheet.confirmLoc.setOnClickListener {

            val intent = Intent(applicationContext,ManuallyAddAddressActivity::class.java)
            intent.putExtra(Constants.ADDRESS,addressText)
            intent.putExtra(Constants.CITY,city)
            intent.putExtra(Constants.STATE,state)
            intent.putExtra(Constants.ZIP,zip)
            startActivity(intent)
            finish()
        }

        val sharePref = getSharedPreferences(Constants.CREATE_USER_PROFILE, MODE_PRIVATE)
        sharePref.edit().putString(Constants.USER_CITY,city).apply()
        sharePref.edit().putString(Constants.USER_STATE,state).apply()
        sharePref.edit().putString(Constants.USER_ZIP,zip).apply()
        sharePref.edit().putString(Constants.USER_LAT,lat.toString()).apply()
        sharePref.edit().putString(Constants.USER_LONG,long.toString()).apply()

//    CommonMethods.setStatusBarColor(window, applicationContext, binding.root, this)
    zoomToCurrentLocation()
     getUserLocation()

    }


    private fun updateLocationText(latLng: LatLng) {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        if (addresses != null) {
            if (addresses.isNotEmpty()) {
                val address = addresses[0]
                 city = address.locality ?: "Unknown city"
                 country = address.countryName ?: "Unknown country"
                 addressText = address.getAddressLine(0) ?: "Unknown address"
//                if(!address.postalCode.isNullOrEmpty()){
                    zip =  address.postalCode ?: "Unknown zip"
//                }

                lat = address.latitude
                long = address.longitude
                 state = address.adminArea ?: "Unknown state"
                binding.bottomSheet.locationTitle.text = addressText
                binding.bottomSheet.fullTextLoc.text = city+","+country


            } else {
                binding.bottomSheet.locationTitle.text = "Lat: ${latLng.latitude}, Lng: ${latLng.longitude}"

            }
        }
    }


    private fun zoomToCurrentLocation() {
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(applicationContext, MapAddressActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun fetchPlaceDetails(placeId: String) {
        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        val request = FetchPlaceRequest.newInstance(placeId, placeFields)

        placesClient.fetchPlace(request).addOnSuccessListener { response ->
            val place = response.place
            val latLng = place.latLng
            if (latLng != null) {
                moveMarker(latLng)

            }
        }.addOnFailureListener { exception ->
//            Log.e("MainActivity", "Place not found: ${exception.message}")
        }
    }

    private fun moveMarker(latLng: LatLng) {
        currentMarker.position = latLng
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        searchResults.visibility = View.GONE
        updateLocationText(latLng)
//        updateLocationText(currentMarker.position)

    }


    private fun searchPlaces(query: String, currentLatLng: LatLng) {
        val bounds = RectangularBounds.newInstance(
            LatLng(currentLatLng.latitude - 0.1, currentLatLng.longitude - 0.1),
            LatLng(currentLatLng.latitude + 0.1, currentLatLng.longitude + 0.1)
        )

        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .setLocationBias(bounds)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                val predictions = response.autocompletePredictions
                searchResultsAdapter.updateData(predictions)
                searchResults.visibility = View.VISIBLE
                enableMapGestures()
            }
            .addOnFailureListener { exception ->
                // Handle failure
                enableMapGestures()
            }
    }

    private fun getBitmapFromDrawable(drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(applicationContext, drawableId)!!
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    private fun getAutocompletePredictions(query: String) {
        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
            val predictions = response.autocompletePredictions
            searchResultsAdapter.updateData(predictions)
        }
    }


    private fun processNearbyPlaces(response: FindCurrentPlaceResponse) {
        for (placeLikelihood in response.placeLikelihoods) {
            val place = placeLikelihood.place
            // Process nearby place
            val name = place.name
            val latLng = place.latLng
            if (name != null && latLng != null) {
                // Do something with the nearby place name and LatLng
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocationAndSearch(query: String) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                searchPlaces(query, currentLatLng)
            } else {
                // Handle location being null
            }
        }
    }

    private fun disableMapGestures() {
        map.uiSettings.isScrollGesturesEnabled = false
        map.uiSettings.isZoomGesturesEnabled = false
    }

    private fun enableMapGestures() {
        map.uiSettings.isScrollGesturesEnabled = true
        map.uiSettings.isZoomGesturesEnabled = true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.uiSettings.isCompassEnabled = false
        map.uiSettings.isMyLocationButtonEnabled = false

        // Move camera to the marker position
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 10f))

        // Add a marker to a default location and move the camera
        val defaultLocation = LatLng(-34.0, 151.0)
        currentMarker = map.addMarker(
            MarkerOptions()
                .position(defaultLocation)
                .draggable(true)
                .title("Selected Location")
        )!!
        currentMarker.isVisible = false
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15f))


        // Set up marker drag listener
        map.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker) {
                // Do something when the marker drag starts
            }

            override fun onMarkerDrag(marker: Marker) {
                // Do something while the marker is being dragged
            }

            override fun onMarkerDragEnd(marker: Marker) {
                // Do something when the marker drag ends
//                Log.d("inside", "drag end")
            }
        })

        // Set up camera move listener
        map.setOnCameraMoveListener {
            // Update marker position to the center of the camera
            val center = map.cameraPosition.target
            currentMarker.position = center
        }

        // Set up camera idle listener
        map.setOnCameraIdleListener {
            // Update location text when the camera stops moving
            updateLocationText(map.cameraPosition.target)
        }

        // Set up map click listener
        map.setOnMapClickListener {
            // Hide search results when the map is clicked
            searchResults.visibility = View.GONE
        }
    }
    private var currentLat: Double? = null
    private var currentLng: Double? = null

    private fun getUserLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)

        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        currentLat = location.latitude
                        currentLng = location.longitude
                        // Update the adapter with the new location
                        searchResultsAdapter.updateCurrentLocation(currentLat, currentLng)
                    }
                }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

}