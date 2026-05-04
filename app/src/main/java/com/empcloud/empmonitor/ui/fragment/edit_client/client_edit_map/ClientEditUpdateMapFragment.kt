package com.empcloud.empmonitor.ui.fragment.edit_client.client_edit_map

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
import com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail
import com.empcloud.empmonitor.databinding.FragmentClientEditUpdateMapBinding
import com.empcloud.empmonitor.ui.adapters.SearchResultsAdapter
import com.empcloud.empmonitor.ui.fragment.client.addcompleteaddress.ClientCompleteAddressFragment
import com.empcloud.empmonitor.ui.fragment.client.clientaddress.ClientAddressFragment
import com.empcloud.empmonitor.ui.fragment.edit_client.client_address.EditClientAddressFragment
import com.empcloud.empmonitor.ui.fragment.edit_client.update_client.UpdaeEditClientFragment
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
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class ClientEditUpdateMapFragment : Fragment(),OnMapReadyCallback {

    private lateinit var binding:FragmentClientEditUpdateMapBinding

    private var clientDetails: ClientFetchDetail? = null

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
    private var latitude: Double? = null
    private var longitude: Double? = null
    private var countryCode:String? = null
    private var countryName:String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentClientEditUpdateMapBinding.inflate(layoutInflater,container,false)

        clientDetails = arguments?.getSerializable(Constants.CLIENT_DETIALS_BUNDLE) as? ClientFetchDetail
//        Log.d("thrher",clientDetails.toString())

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

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.currentlocbtn.setOnClickListener {
            binding.searchResults.visibility = View.GONE
            zoomToCurrentLocation()
        }

        binding.backbtnclient.setOnClickListener {

            val fragment = EditClientAddressFragment()
            val args = Bundle().apply {

                CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.CLIENT_DETIALS_BUNDLE)
                putSerializable(Constants.CLIENT_DETIALS_BUNDLE,clientDetails)
            }
            fragment.arguments = args
            CommonMethods.switchFragment(requireActivity(),fragment)
        }

        binding.bottomSheet.confirmLoc.setOnClickListener {

            val fragment = EditClientAddressFragment()
            val args = Bundle().apply {
                putString(Constants.ADDRESS_CLIENT,addressText)
                putString(Constants.CITY_UPDATE,city)
                putString(Constants.STATE_UPDATE,state)
                putString(Constants.ZIP_UPDATE,zip)
                putDouble(Constants.LAT_UPDATE,latitude!!)
                putDouble(Constants.LON_UPDATE,longitude!!)
                putString(Constants.COUNTRYCODE,countryCode)
                putString(Constants.COUNTRYNAME,countryName)


//                Log.d("thrher",latitude.toString())
//                Log.d("thrher",longitude.toString())

                putSerializable(Constants.CLIENT_DETIALS_BUNDLE,clientDetails)

            }
            fragment.arguments= args
            CommonMethods.switchFragment(requireActivity(),fragment)
        }

        zoomToCurrentLocation()
        getUserLocation()
    }
    fun doubleToLong(doubleValue: Double): Long {
        return doubleValue.toLong()
    }


//    private fun updateLocationText(latLng: LatLng) {
//        val geocoder = Geocoder(requireContext(), Locale.getDefault())
//        val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
//        if (addresses != null) {
//            if (addresses.isNotEmpty()) {
//                val address = addresses[0]
//                city = address.locality
//                country = address.countryName
//                addressText = address.getAddressLine(0)
//                if(!address.postalCode.isNullOrEmpty()){
//                    zip =  address.postalCode
//                }
//                state = address.adminArea
//                latitude = address.latitude
//                longitude =address.longitude
//                countryCode = address.countryCode
//                countryName = address.countryName
//                binding.bottomSheet.locationTitle.text = addressText
//                binding.bottomSheet.fullTextLoc.text = city+","+country
//            } else {
//                binding.bottomSheet.locationTitle.text = "Lat: ${latLng.latitude}, Lng: ${latLng.longitude}"
//
//            }
//        }
//    }

    private fun updateLocationText(latLng: LatLng) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        if (addresses != null && addresses.isNotEmpty()) {
            val address = addresses[0]
            city = address.locality ?: "Unknown city"
            country = address.countryName ?: "Unknown country"
            addressText = address.getAddressLine(0) ?: "Unknown address"
            zip = address.postalCode ?: "Unknown zip"
            state = address.adminArea ?: "Unknown state"
            latitude = address.latitude
            longitude = address.longitude
            countryCode = address.countryCode ?: "Unknown country code"
            countryName = address.countryName ?: "Unknown country name"

            binding.bottomSheet.locationTitle.text = addressText
            binding.bottomSheet.fullTextLoc.text = "$city, $country"
        } else {
            binding.bottomSheet.locationTitle.text = "Lat: ${latLng.latitude}, Lng: ${latLng.longitude}"
        }
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