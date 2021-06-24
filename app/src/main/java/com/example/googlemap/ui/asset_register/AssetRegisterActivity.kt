package com.example.googlemap.ui.asset_register

import android.annotation.SuppressLint
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
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
    private var map: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asset_register)

        setUpMap()
    }

    @SuppressLint("MissingPermission")
    private fun setUpMap() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager.findFragmentById(
            R.id.asset_register_map
        ) as? SupportMapFragment
        mapFragment?.getMapAsync { googleMap ->
            map = googleMap
            googleMap.isMyLocationEnabled = true
            // Info Window Content
            googleMap.setInfoWindowAdapter(MarkerAssetInfoAdapter(this))
            // get current location & set camera focus
            getCurrentLocation {
                val pos = CameraPosition.fromLatLngZoom(it.latLng, 13f)
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(pos))
            }
        }
    }

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.get_asset_radius_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.option_get_asset) {
            getAssetByRadius()
        }
        return true
    }

    private fun getAssetByRadius() {
        map?.let {
            Toast.makeText(this, "Finding asset..", Toast.LENGTH_SHORT).show()
            setMarkers(it)
        }
    }
}