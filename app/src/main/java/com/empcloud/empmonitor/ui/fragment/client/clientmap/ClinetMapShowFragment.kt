package com.empcloud.empmonitor.ui.fragment.client.clientmap

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.databinding.FragmentClinetMapShowBinding
import com.empcloud.empmonitor.ui.adapters.SearchResultsAdapter
import com.empcloud.empmonitor.ui.fragment.client.add_client_with_editoptions.AddClientEditOptionFragment
import com.empcloud.empmonitor.ui.fragment.client.addcompleteaddress.ClientCompleteAddressFragment
import com.empcloud.empmonitor.ui.fragment.client.clientaddress.ClientAddressFragment
import com.empcloud.empmonitor.ui.fragment.client.clientdirection.ClientDirectionFragment
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import com.empcloud.empmonitor.utils.NativeLib
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class ClinetMapShowFragment : Fragment(),OnMapReadyCallback {

    private var latitude: Double? = null
    private var longitude: Double? = null

    private lateinit var binding: FragmentClinetMapShowBinding

    private lateinit var map: GoogleMap
    private lateinit var currentMarker: Marker
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var city:String
    private lateinit var country:String
    private lateinit var addressText:String
    private lateinit var zip: String
    private lateinit var state:String
    private lateinit var searchResults: RecyclerView
    private lateinit var searchBar: EditText
    private lateinit var searchResultsAdapter: SearchResultsAdapter
    private lateinit var placesClient: PlacesClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentClinetMapShowBinding.inflate(layoutInflater,container,false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapClient) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Places.initialize(requireContext(), NativeLib().getGoogleMapApiKey())
        placesClient = Places.createClient(requireContext())

        searchBar = binding.searchBar
        searchResults = binding.searchResults
        searchResults.layoutManager = LinearLayoutManager(requireContext())
        searchResultsAdapter = SearchResultsAdapter(placesClient){ placeId ->
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



        binding.currentlocbtn.setOnClickListener {
            binding.searchResults.visibility = View.GONE
            zoomToCurrentLocation()
        }

        binding.backbtnclient.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,ClientAddressFragment()).commit()

        }

        binding.bottomSheet.confirmLoc.setOnClickListener {

//            Log.d("latloncheck","$latitude $longitude")
            val shardPref = requireContext().getSharedPreferences(Constants.CLIENT_DETAILS,AppCompatActivity.MODE_PRIVATE)
            shardPref.edit(). putString(Constants.ADDRESS_CLIENT,addressText).apply()
            shardPref.edit(). putString(Constants.CITY_CLIENT,city).apply()
            shardPref.edit(). putString(Constants.STATE_CLIENT,state).apply()
            shardPref.edit().  putString(Constants.ZIP_CLIENT,zip).apply()
            shardPref.edit().  putString(Constants.COUNTRY_CLIENT_NEW,country).apply()

//            shardPref.edit(). putLong(Constants.LATITUDE_CLIENT, doubleToLong(latitude!!)).apply()
//            shardPref.edit().putLong(Constants.LONGITUDE_CLIENT, doubleToLong(longitude!!)).apply()
//            Log.d("cityDdd",city)

            val clientaddressFragment = ClientCompleteAddressFragment()
            val args = Bundle().apply {

                putDouble(Constants.LATITUDE_CLIENT, latitude!!)
                putDouble(Constants.LONGITUDE_CLIENT, longitude!!)

            }
            clientaddressFragment.arguments = args
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,clientaddressFragment).commit()

        }

        zoomToCurrentLocation()
        getUserLocation()
    }

    fun doubleToLong(doubleValue: Double): Long {
        return doubleValue.toLong()
    }


    private fun updateLocationText(latLng: LatLng) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        if (addresses != null) {
            if (addresses.isNotEmpty()) {
                val address = addresses[0]
                city = address.locality ?: "Unknown city"
                country = address.countryName ?: "Unknown country"
                addressText = address.getAddressLine(0)
//                if(!address.postalCode.isNullOrEmpty()){
                    zip =  address.postalCode ?: "Unknown zip"
//                }
                state = address.adminArea ?: "Unknown state"
                latitude = address.latitude
                longitude =address.longitude
                binding.bottomSheet.locationTitle.text = addressText
                binding.bottomSheet.fullTextLoc.text = city+","+country
            } else {
                binding.bottomSheet.locationTitle.text = "Lat: ${latLng.latitude}, Lng: ${latLng.longitude}"

            }
        }

        CommonMethods.saveSharedPrefernce(requireActivity(),Constants.LAT_CLIENT_ADD,Constants.LAT_CLIENT_ADD,latitude.toString())
        CommonMethods.saveSharedPrefernce(requireActivity(),Constants.LON_CLIENT_ADD,Constants.LON_CLIENT_ADD,longitude.toString())

    }


    private fun zoomToCurrentLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_FINE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_COARSE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
            }
        }
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

    }


    private fun searchPlaces(query: String, currentLatLng: LatLng) {
        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                val predictions = response.autocompletePredictions
                searchResultsAdapter.updateData(predictions)
                searchResults.visibility = View.VISIBLE
                enableMapGestures()
            }
            .addOnFailureListener { exception ->
//                Log.e("MainActivity", "Place not found: ${exception.message}")
                enableMapGestures()
            }
    }

    private fun getBitmapFromDrawable(drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(requireContext(), drawableId)!!
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

    private fun disableMapGestures() {
        map.uiSettings.isScrollGesturesEnabled = false
        map.uiSettings.isZoomGesturesEnabled = false
    }

    private fun enableMapGestures() {
        map.uiSettings.isScrollGesturesEnabled = true
        map.uiSettings.isZoomGesturesEnabled = true
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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

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