package com.empcloud.empmonitor.ui.adapters
import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.empcloud.empmonitor.R
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.squareup.picasso.Picasso
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class SearchResultsAdapter(private val placesClient: PlacesClient,private val onPlaceSelected: (String) -> Unit) :
    RecyclerView.Adapter<SearchResultsAdapter.SearchResultViewHolder>() {

    private var results: List<AutocompletePrediction> = listOf()
    private var currentLat: Double? = null
    private var currentLng: Double? = null

    fun updateData(newResults: List<AutocompletePrediction>) {
        results = newResults
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mapsearchitems, parent, false)
        return SearchResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        val result = results[position]
        holder.textView.text = result.getPrimaryText(null)
        holder.itemView.setOnClickListener {
            onPlaceSelected(result.placeId)
        }
       holder.textView2.text = result.getSecondaryText(null)

        if (position == itemCount - 1) {
            holder.divider.visibility = View.GONE
        } else {
            holder.divider.visibility = View.VISIBLE
        }
        fetchPlaceDetails(result.placeId) { place, iconUrl ->
            if (place != null && place.latLng != null) {
                if (currentLat != null && currentLng != null) {
                    val placeLatLng = place.latLng
                    val distance = calculateDistance(currentLat!!, currentLng!!, placeLatLng.latitude, placeLatLng.longitude)
                    holder.distance.text = String.format("%.2f km", distance)
                }

                if (iconUrl != null) {
                    holder.icon.setImageResource(R.drawable.location_new)
                } else {
                    holder.icon.setImageResource(R.drawable.location_new) // Set a default icon
                }
            }
        }

    }

    override fun getItemCount(): Int = results.size

    class SearchResultViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.titletext)
        val textView2:TextView = view.findViewById(R.id.secondaryttx)
        val icon:ImageView = view.findViewById(R.id.place_Icon)
        val distance:TextView = view.findViewById(R.id.distance)
        val divider:View = view.findViewById(R.id.divider)
    }

    private fun fetchPlaceDetails(placeId: String, callback: (Place?, String?) -> Unit) {
        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ICON_URL)
        val request = FetchPlaceRequest.newInstance(placeId, placeFields)

        placesClient.fetchPlace(request).addOnSuccessListener { response ->
            val place = response.place
            val iconUrl = place.iconUrl
            callback(place, iconUrl)
        }.addOnFailureListener { exception ->
            callback(null, null)
        }
    }

    private fun calculateDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lng1, lat2, lng2, results)
        return results[0].toDouble() / 1000 // Convert meters to kilometers
    }

    fun updateCurrentLocation(lat: Double?, lng: Double?) {
        currentLat = lat
        currentLng = lng
        notifyDataSetChanged()
    }


}
