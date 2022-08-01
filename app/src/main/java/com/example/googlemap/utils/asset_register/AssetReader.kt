package com.example.googlemap.utils.asset_register

import android.content.Context
import android.location.Location
import com.example.googlemap.R
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStream
import java.io.InputStreamReader

class AssetReader(private val context: Context) {

    private val gson = Gson()

    private val inputStream: InputStream
        get() = context.resources.openRawResource(R.raw.asset)

    fun read(): List<Asset> {
        val itemType = object : TypeToken<List<AssetResponse>>() {}.type
        val reader = InputStreamReader(inputStream)
        return gson.fromJson<List<AssetResponse>>(reader, itemType).map {
            it.toAsset()
        }
    }

    fun setRandomAsset(myLocation: Location): List<Asset> {
        val randomAsset = ArrayList<Asset>()

        for (i in 0..99) {
            val randomLat = myLocation.latitude + Math.random() - Math.random()
            val randomLng = myLocation.longitude + Math.random() - Math.random()
            randomAsset.add( Asset(
                    name = "Asset $i",
                    latLng = LatLng(randomLat, randomLng),
                    address = "Jalan Asset $i"
            ))
        }

        return randomAsset
    }
}