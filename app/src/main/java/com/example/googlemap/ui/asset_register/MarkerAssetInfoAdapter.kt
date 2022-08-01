package com.example.googlemap.ui.asset_register

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.example.googlemap.R
import com.example.googlemap.utils.asset_register.Asset
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class MarkerAssetInfoAdapter(
    private val context: Context
) : GoogleMap.InfoWindowAdapter {

    override fun getInfoWindow(marker: Marker?): View? {
        // Return null to indicate that the
        // default window (white bubble) should be used
        return null
    }

    override fun getInfoContents(marker: Marker?): View? {
        // 1. Get tag
        val asset = marker?.tag as? Asset ?: return null

        // 2. Inflate view and set title, address, and rating
        val view = LayoutInflater.from(context).inflate(
            R.layout.marker_asset_info, null
        )
        view.findViewById<TextView>(R.id.tv_title).text = asset.name
        view.findViewById<TextView>(R.id.tv_address).text = asset.address
        view.findViewById<TextView>(R.id.tv_lat).text = "lat:${asset.latLng.latitude}"
        view.findViewById<TextView>(R.id.tv_lng).text = "lng:${asset.latLng.longitude}"

        return view
    }
}