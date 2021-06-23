package com.example.googlemap.ui.asset_register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.googlemap.R
import com.example.googlemap.utils.asset_register.Asset
import com.example.googlemap.utils.asset_register.AssetReader
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class AssetRegisterActivity : AppCompatActivity() {

    private val asset: List<Asset> by lazy {
        AssetReader(this).read()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asset_register)

        setUpMap()
    }

    /**
     * reference to the GoogleMap object
     */
    private fun setUpMap() {
        val mapFragment = supportFragmentManager.findFragmentById(
            R.id.asset_register_map
        ) as? SupportMapFragment
        mapFragment?.getMapAsync { googleMap ->
            // set camera focus
            googleMap.setOnMapLoadedCallback {
                val bounds = LatLngBounds.builder()
                asset.forEach { bounds.include(it.latLng) }
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 18))
            }
            // Info Window Content
            googleMap.setInfoWindowAdapter(MarkerAssetInfoAdapter(this))
            // set mark
            setMarkers(googleMap)
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
            )
            marker.tag = data
        }
    }
}