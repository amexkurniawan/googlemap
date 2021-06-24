package com.example.googlemap.ui.asset_register

import android.annotation.SuppressLint
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.googlemap.R
import com.example.googlemap.ui.lesson2.latLng
import com.example.googlemap.utils.asset_register.Asset
import com.example.googlemap.utils.asset_register.AssetReader
import com.example.googlemap.utils.helper.BitmapHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MarkerOptions

class AssetRegisterActivity : AppCompatActivity() {

    private val asset: List<Asset> by lazy {
        AssetReader(this).read()
    }

    private val assetIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(this, R.color.purple_700)
        BitmapHelper.vectorToBitmap(this, R.drawable.ic_baseline_place_24, color)
    }

    private val TAG = "AssetRegisterActivity"
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asset_register)

        setUpMap()
    }

    /**
     * reference to the GoogleMap object
     */
    @SuppressLint("MissingPermission")
    private fun setUpMap() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager.findFragmentById(
            R.id.asset_register_map
        ) as? SupportMapFragment
        mapFragment?.getMapAsync { googleMap ->
            googleMap.isMyLocationEnabled = true
            // Info Window Content
            googleMap.setInfoWindowAdapter(MarkerAssetInfoAdapter(this))
            // get current location & set camera focus
            getCurrentLocation {
                val pos = CameraPosition.fromLatLngZoom(it.latLng, 13f)
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(pos))
                setMarkers(googleMap)
            }
        }
    }

    /**
     * Adds marker representations of the places list on the provided GoogleMap object
     */
    private fun setMarkers(googleMap: GoogleMap) {
        asset.forEach { data ->
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .title(data.name)
                    .position(data.latLng)
                    .icon(assetIcon)
            )
            marker.tag = data
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(onSuccess: (Location) -> Unit) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            currentLocation = location
            onSuccess(location)
        }.addOnFailureListener {
            Log.e(TAG, "Could not get location")
        }
    }
}