package com.example.googlemap.utils.asset_register

import com.google.android.gms.maps.model.LatLng

data class PlaceResponse(
    val geometry: Geometry,
    val name: String,
    val address: String
) {

    data class Geometry(
        val location: GeometryLocation
    )

    data class GeometryLocation(
        val lat: Double,
        val lng: Double
    )
}

fun PlaceResponse.toPlace(): Asset = Asset(
    name = name,
    latLng = LatLng(geometry.location.lat, geometry.location.lng),
    address = address
)
