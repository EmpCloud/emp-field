package com.empcloud.empmonitor.ui.fragment.edit_client.update_client

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail
import com.empcloud.empmonitor.data.remote.response.map_response.DirectionsResponse
import com.empcloud.empmonitor.databinding.FragmentUpdaeEditClientBinding
import com.empcloud.empmonitor.ui.adapters.SearchResultsAdapter
import com.empcloud.empmonitor.ui.fragment.attendance.AttendanceFragment
import com.empcloud.empmonitor.ui.fragment.client.clientdirection.ClientDirectionViewModel
import com.empcloud.empmonitor.ui.fragment.client.clienthome.ClientHomeFragment
import com.empcloud.empmonitor.ui.fragment.edit_client.first.EditClientFragment
import com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener
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
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.PolyUtil
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class UpdaeEditClientFragment constructor(private val listener: OnFragmentChangedListener? = null) : Fragment(),OnMapReadyCallback {

    private lateinit var binding:FragmentUpdaeEditClientBinding
    private val viewModel by viewModels<ClientDirectionViewModel>()

    private lateinit var searchResults: RecyclerView
    private lateinit var searchBar: EditText
    private lateinit var searchResultsAdapter: SearchResultsAdapter
    private lateinit var placesClient: PlacesClient
    private lateinit var map: GoogleMap
    private lateinit var currentMarker: Marker
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var clientDetails:ClientFetchDetail? = null
    private var lat:Double? = null
    private var lon:Double? = null


    companion object{
        fun newInstance() = UpdaeEditClientFragment

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        clientDetails = arguments?.getSerializable(Constants.CLIENT_DETIALS_BUNDLE) as? ClientFetchDetail
        lat = clientDetails?.latitude?.toDouble()
        lon = clientDetails?.longitude?.toDouble()
//        Log.d("latloncheck","$lat $lon")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpdaeEditClientBinding.inflate(layoutInflater,container,false)

        Places.initialize(requireContext(),NativeLib().getGoogleMapApiKey())
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
                    searchPlaces(s.toString())
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

        val phoneNumber = clientDetails!!.contactNumber

        binding.bottomSheet.selectDirection.visibility = View.VISIBLE
        binding.bottomSheet.direction.visibility = View.GONE

        binding.bottomSheet.name.setText(clientDetails!!.clientName)
        binding.bottomSheet.address.setText(clientDetails!!.address1)
        if (!clientDetails!!.clientProfilePic.isNullOrEmpty()){

            binding.bottomSheet.picard.visibility = View.VISIBLE
            binding.bottomSheet.proficdisable.visibility = View.GONE
            Picasso.get().load(clientDetails!!.clientProfilePic).into(binding.bottomSheet.profilePic)
        }

        binding.bottomSheet.editbtn.setOnClickListener {

            val fragment = EditClientFragment(listener)
            val args = Bundle().apply {
                putSerializable(Constants.CLIENT_DETIALS_BUNDLE,clientDetails)
                putInt("FramentCameFrom",300)
            }
            fragment.arguments = args
            CommonMethods.switchFragment(requireActivity(),fragment)
        }

        binding.bottomSheet.Call.setOnClickListener {

            binding.bottomSheet.Call.visibility = View.GONE
            binding.bottomSheet.selectCall.visibility = View.VISIBLE
            binding.bottomSheet.message.visibility = View.VISIBLE
            binding.bottomSheet.direction.visibility = View.VISIBLE

            binding.bottomSheet.selectMessage.visibility = View.GONE
            binding.bottomSheet.selectDirection.visibility =View.GONE

            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${phoneNumber}")
            }
            startActivity(intent)
            // Verify that the intent can be resolved before starting
        }

        binding.bottomSheet.selectCall.setOnClickListener {

            binding.bottomSheet.Call.visibility = View.GONE
            binding.bottomSheet.selectCall.visibility = View.VISIBLE
            binding.bottomSheet.message.visibility = View.VISIBLE
            binding.bottomSheet.direction.visibility = View.VISIBLE

            binding.bottomSheet.selectMessage.visibility = View.GONE
            binding.bottomSheet.selectDirection.visibility =View.GONE

            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${phoneNumber}")
            }
            startActivity(intent)
            // Verify that the intent can be resolved before starting
        }

        binding.bottomSheet.message.setOnClickListener {

            binding.bottomSheet.selectMessage.visibility = View.VISIBLE
            binding.bottomSheet.message.visibility = View.GONE


            binding.bottomSheet.Call.visibility = View.VISIBLE
            binding.bottomSheet.selectCall.visibility = View.GONE
            binding.bottomSheet.selectDirection.visibility =View.GONE
            binding.bottomSheet.direction.visibility = View.VISIBLE

            val uri = Uri.parse("smsto:${phoneNumber}")
            val intent = Intent(Intent.ACTION_SENDTO, uri)
            startActivity(intent)

        }

        binding.bottomSheet.selectMessage.setOnClickListener {

            binding.bottomSheet.selectMessage.visibility = View.VISIBLE
            binding.bottomSheet.message.visibility = View.GONE


            binding.bottomSheet.Call.visibility = View.VISIBLE
            binding.bottomSheet.selectCall.visibility = View.GONE
            binding.bottomSheet.selectDirection.visibility =View.GONE
            binding.bottomSheet.direction.visibility = View.VISIBLE

            val uri = Uri.parse("smsto:${phoneNumber}")
            val intent = Intent(Intent.ACTION_SENDTO, uri)
            startActivity(intent)

        }

        binding.bottomSheet.direction.setOnClickListener {

            binding.bottomSheet.direction.visibility = View.GONE
            binding.bottomSheet.selectDirection.visibility = View.VISIBLE


            binding.bottomSheet.Call.visibility = View.VISIBLE
            binding.bottomSheet.selectCall.visibility = View.GONE


            binding.bottomSheet.message.visibility = View.VISIBLE
            binding.bottomSheet.selectMessage.visibility = View.GONE

        }

        binding.backbtn.setOnClickListener {

            CommonMethods.switchFragment(requireActivity(),ClientHomeFragment(listener))
        }
        getUserLocation()

    }
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        // Move camera to the marker position
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 10f))

//        // Add a marker to a default location and move the camera
        val defaultLocation = LatLng(-34.0, 151.0)
        currentMarker = map.addMarker(
            MarkerOptions().position(defaultLocation).icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromDrawable(R.drawable.marker_customs))).draggable(true).title("Selected Location"))!!
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15f))

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true

            // Get the current location of the device and set the position of the map
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))

                    val call =  viewModel.mMapApiService.getDirections(
                        "${currentLatLng.latitude},${currentLatLng.longitude}",
                        "$lat,$lon", // Replace with your destination coordinates
                        NativeLib().getGoogleMapApiKey()

                    )
                    call.enqueue(object : Callback<DirectionsResponse> {
                        override fun onResponse(
                            call: Call<DirectionsResponse>,
                            response: Response<DirectionsResponse>
                        ) {
                            if (response.isSuccessful) {
                                binding.centerMarker.visibility = View.GONE
                                val directionsResponse = response.body()
                                if (directionsResponse != null) {
                                    val route = directionsResponse.routes.firstOrNull()
                                    if (route != null) {
                                        val points = route.overview_polyline.points
                                        val decodedPath = PolyUtil.decode(points)
                                        map.addPolyline(
                                            PolylineOptions().addAll(decodedPath).color(
                                                Color.RED).width(10f))
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                            // Handle failure
                            Toast.makeText(requireContext(),"ERROR WHILE DRAWING", Toast.LENGTH_SHORT).show()
                        }
                    })
                    // Add a marker at the current location and disable dragging
                    val markerOptions = MarkerOptions().position(currentLatLng).icon(
                        BitmapDescriptorFactory.fromBitmap(getBitmapFromDrawable(R.drawable.marker_customs))).anchor(0.5f, 0.5f) .title("You are here").draggable(false)
                    map.addMarker(markerOptions)

                    val markerColor = Color.BLUE

                    val bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_BLUE)

//                    val destinationLatLng = LatLng(arguments?.getDouble(Constants.LAT) ?: 0.0, arguments?.getDouble(
//                        Constants.LON) ?: 0.0)

                    val destinationLatLng = LatLng(lat!!,lon!!)
                    val destinationMarkerOptions = MarkerOptions().position(destinationLatLng).title("Destination").icon(bitmapDescriptor)
                    map.addMarker(destinationMarkerOptions)
                }
            }
        }

        currentMarker.isVisible = false

        map.setOnCameraMoveListener {
            val center = map.cameraPosition.target
            currentMarker.position = center
        }

        map.setOnMapClickListener {
            searchResults.visibility = View.GONE
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


    private fun searchPlaces(query: String) {
        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                val predictions = response.autocompletePredictions
                searchResultsAdapter.updateData(predictions)
                searchResults.visibility = View.VISIBLE
            }
            .addOnFailureListener { exception ->
//                Log.e("MainActivity", "Place not found: ${exception.message}")
            }
    }


}