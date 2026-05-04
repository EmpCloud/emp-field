package com.empcloud.empmonitor.ui.fragment.client.add_client_with_editoptions

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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail
import com.empcloud.empmonitor.data.remote.response.map_response.DirectionsResponse
import com.empcloud.empmonitor.databinding.FragmentAddClientEditOptionBinding
import com.empcloud.empmonitor.databinding.FragmentClientDirectionBinding
import com.empcloud.empmonitor.ui.adapters.SearchResultsAdapter
import com.empcloud.empmonitor.ui.fragment.client.addcompleteaddress.ClientCompleteAddressFragment
import com.empcloud.empmonitor.ui.fragment.client.clientdirection.ClientDirectionViewModel
import com.empcloud.empmonitor.ui.fragment.client.clienthome.ClientHomeFragment
import com.empcloud.empmonitor.ui.fragment.client.finalladd.ClientAddFinalFragment
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
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class AddClientEditOptionFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding:FragmentAddClientEditOptionBinding
    private val viewModel by viewModels<ClientDirectionViewModel>()

    private lateinit var searchResults: RecyclerView
    private lateinit var searchBar: EditText
    private lateinit var searchResultsAdapter: SearchResultsAdapter
    private lateinit var placesClient: PlacesClient
    private lateinit var map: GoogleMap
    private lateinit var currentMarker: Marker
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var name:String? = null
    private var address:String? = null

    private var editName:String? = null
    private var editAddress:String? = null
    private  var status:Int? = 0
    private  var mobileNO:String? = null

    private var clientId:String? = null

    private var lat:Double? = null
    private var lon:Double? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        name = arguments?.getString(Constants.DIRECTION_NAME)
        address = arguments?.getString(Constants.DIRECTION_ADDRESS)

        editName = arguments?.getString("Name")
        editAddress = arguments?.getString("Address")
        status = arguments?.getInt("status")
        mobileNO = arguments?.getString("mobileno")

        lat = CommonMethods.getSharedPrefernce(requireActivity(),Constants.LAT_CLIENT_ADD).toDouble()
        lon = CommonMethods.getSharedPrefernce(requireActivity(),Constants.LON_CLIENT_ADD).toDouble()
//        Log.d("aksjdlkjsd","{$lat $lon}")

//        clientId = arguments?.getString(Constants.CLIENT_ID_CREATED)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddClientEditOptionBinding.inflate(layoutInflater,container,false)
        Places.initialize(requireContext(),"AIzaSyCttW09coBAF-u_FvqOiriGmLHs_SFTY0s")
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

        binding.bottomSheet.selectDirection.visibility = View.VISIBLE
        binding.bottomSheet.direction.visibility = View.GONE

        if (status == 1){
            binding.bottomSheet.name.setText(editName)
            binding.bottomSheet.address.setText(editAddress)

        }else{
            binding.bottomSheet.name.setText(name)
            binding.bottomSheet.address.setText(address)
        }


        binding.backbtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,
                ClientHomeFragment()
            ).commit()
        }

        binding.bottomSheet.editbtn.setOnClickListener {

            val fragment = ClientAddFinalFragment()
            val args = Bundle().apply {
                putInt(Constants.EDIT_BACK,100)
            }
            fragment.arguments = args
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,
                fragment
            ).commit()
        }

        binding.bottomSheet.Call.setOnClickListener {

            binding.bottomSheet.Call.visibility = View.GONE
            binding.bottomSheet.selectCall.visibility = View.VISIBLE
            binding.bottomSheet.message.visibility = View.VISIBLE
            binding.bottomSheet.direction.visibility = View.VISIBLE

            binding.bottomSheet.selectMessage.visibility = View.GONE
            binding.bottomSheet.selectDirection.visibility =View.GONE

            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$mobileNO")
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

            val uri = Uri.parse("smsto:${mobileNO}")
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
                        "${lat},${lon}", // Replace with your destination coordinates
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

                    val destinationLatLng = LatLng(lat ?: 0.0, lon?: 0.0)
                    val destinationMarkerOptions = MarkerOptions().position(destinationLatLng).title("Destination").icon(bitmapDescriptor)
                    map.addMarker(destinationMarkerOptions)
                }
            }
        }
        currentMarker.isVisible = false



        map.setOnCameraMoveListener {
            val center = map.cameraPosition.target
            currentMarker.position = center
//            updateLocationText(center)
        }

//
        map.setOnMapClickListener {
            searchResults.visibility = View.GONE
        }    }

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

}