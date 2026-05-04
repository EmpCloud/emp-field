package com.empcloud.empmonitor.ui.fragment.update_address

import android.annotation.SuppressLint
import android.content.Intent
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
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.databinding.FragmentUpdateAddressMapBinding
import com.empcloud.empmonitor.ui.activity.mapaddress.ManuallyAddAddressActivity
import com.empcloud.empmonitor.ui.activity.mapaddress.MapAddressActivity
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
class UpdateAddressMapFragment : Fragment(),OnMapReadyCallback {

    private lateinit var binding:FragmentUpdateAddressMapBinding
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

    private var lat: Double? = null
    private var long:Double? = null

    private var fragmentNo:Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentNo = arguments?.getInt("fragementMap")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpdateAddressMapBinding.inflate(layoutInflater,container,false)

        Places.initialize(requireContext(), NativeLib().getGoogleMapApiKey())
        placesClient = Places.createClient(requireContext())

        searchBar = binding.searchBar
        searchResults = binding.searchResults
        searchResults.layoutManager = LinearLayoutManager(requireContext())
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

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.currentlocbtn.setOnClickListener {
            binding.searchResults.visibility = View.GONE
            zoomToCurrentLocation()
        }

        binding.backbtnupdate.setOnClickListener {

            CommonMethods.switchFragment(requireActivity(),UpdateAddressFragment())

        }

        binding.bottomSheet.confirmLoc.setOnClickListener {

            if (fragmentNo == 2001){

                val fragmentChanged = UpdateFullAddressFragment()
                val args = Bundle().apply {
                    putString(Constants.UPDATE_ADDRESS,addressText)
                    putString(Constants.UPDATE_CITY,city)
                    putString(Constants.UPDATE_STATE,state)
                    putString(Constants.UPDATE_ZIP,zip)
                    putDouble(Constants.UPDATE_LAT,lat!!)
                    putDouble(Constants.UPDATE_LON,long!!)

                }
                fragmentChanged.arguments = args
                CommonMethods.switchFragment(requireActivity(),fragmentChanged)
            }else
            {
                val fragmentChanged = UpdateFullAddressFragment()
                val args = Bundle().apply {
                    putString(Constants.UPDATE_ADDRESS,addressText)
                    putString(Constants.UPDATE_CITY,city)
                    putString(Constants.UPDATE_STATE,state)
                    putString(Constants.UPDATE_ZIP,zip)
                    putDouble(Constants.UPDATE_LAT,lat!!)
                    putDouble(Constants.UPDATE_LON,long!!)

                }
                fragmentChanged.arguments = args
                CommonMethods.switchFragment(requireActivity(),fragmentChanged)
            }

        }

        zoomToCurrentLocation()
        getUserLocation()

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

                lat = address.latitude
                long = address.longitude
                state = address.adminArea ?: "Unknown state"
                binding.bottomSheet.locationTitle.text = addressText
                binding.bottomSheet.fullTextLoc.text = city+","+country
                CommonMethods.saveSharedPrefernce(requireActivity(),Constants.UPDATE_PROFILE_LAT,Constants.UPDATE_PROFILE_LAT,lat.toString())
                CommonMethods.saveSharedPrefernce(requireActivity(),Constants.UPDATE_PROFILE_LON,Constants.UPDATE_PROFILE_LON,long.toString())



            } else {
                binding.bottomSheet.locationTitle.text = "Lat: ${latLng.latitude}, Lng: ${latLng.longitude}"

            }
        }
    }


    private fun zoomToCurrentLocation() {
        if (checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_FINE_LOCATION) != PermissionChecker.PERMISSION_GRANTED &&
            checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_COARSE_LOCATION) != PermissionChecker.PERMISSION_GRANTED) {
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
                Log.d("inside", "drag end")
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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

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