package com.example.googlemap.ui.asset_register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.googlemap.R
import com.google.android.gms.maps.SupportMapFragment

class AssetRegisterActivity : AppCompatActivity() {
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
            // set mark
        }
    }
}