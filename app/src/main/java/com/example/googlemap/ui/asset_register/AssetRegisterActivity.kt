package com.example.googlemap.ui.asset_register

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.googlemap.R
import com.example.googlemap.ui.lesson2.latLng
import com.example.googlemap.utils.asset_register.Asset
import com.example.googlemap.utils.asset_register.AssetReader
import com.example.googlemap.utils.extentions.isGpsEnabled
import com.example.googlemap.utils.extentions.isLocationPermissionGranted
import com.example.googlemap.utils.extentions.requestLocationPermission
import com.example.googlemap.utils.helper.BitmapHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_asset_register.*

class AssetRegisterActivity : AppCompatActivity(), View.OnClickListener {

    private val asset: List<Asset> by lazy {
        AssetReader(this).read()
    }

    private val assetIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(this, R.color.purple_700)
        BitmapHelper.vectorToBitmap(this, R.drawable.ic_baseline_place_24, color)
    }

    private val TAG = "AssetRegisterActivity"
    private val REQUEST_ACCESS_FINE_LOCATION = 101
    private var map: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: Location? = null
    private var radius = 1000 // (meter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asset_register)

        checkPermissionAndGps()
        setView()
    }

    override fun onResume() {
        super.onResume()
        checkPermissionAndGps()
    }

    private fun checkPermissionAndGps() {
        when {
            !isLocationPermissionGranted -> requestLocationPermission(REQUEST_ACCESS_FINE_LOCATION)
            !isGpsEnabled -> {
                Toast.makeText(this, "please enable your gps!", Toast.LENGTH_SHORT).show()
                finish()
            }
            else -> setUpMap()
        }
    }

    private fun setView() {
        btnMinus.setOnClickListener(this)
        btnPlus.setOnClickListener(this)
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

    private fun setMarkers(googleMap: GoogleMap, asset: List<Asset>) {
        if (asset.isNullOrEmpty()) {
            googleMap.clear()
            Toast.makeText(this, "No asset in radius $radius meter.", Toast.LENGTH_SHORT).show()
        } else {
            googleMap.clear()
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
    }

    private fun getAssetByRadius(myLocation: Location) {
        map?.let {
            val assetInRadius = ArrayList<Asset>()
            var location = Location("")

            asset.forEach {
                location.latitude = it.latLng.latitude
                location.longitude = it.latLng.longitude

                if (myLocation.distanceTo(location) <= radius) {
                    assetInRadius.add(it)
                }
            }

            setMarkers(it, assetInRadius)
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
            currentLocation?.let { getAssetByRadius(it) }
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.size == 1 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "location permission denied", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.btnMinus -> {
                if (radius == 1000) {
                    radius
                } else {
                    radius = radius - 1000
                    findViewById<TextView>(R.id.tvNumberOfRadius).text = (radius/1000).toString()
                }
            }
            R.id.btnPlus -> {
                radius = radius + 1000
                findViewById<TextView>(R.id.tvNumberOfRadius).text = (radius/1000).toString()
            }
        }
    }
}