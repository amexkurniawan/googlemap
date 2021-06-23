package com.example.googlemap.utils.asset_register

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.googlemap.R
import com.example.googlemap.utils.helper.BitmapHelper
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

/**
 * A custom cluster renderer for Place objects.
 */
class AssetRenderer(
    private val context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<Asset>
) : DefaultClusterRenderer<Asset>(context, map, clusterManager) {

    /**
     * The icon to use for each cluster item
     */
    private val assetIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(context,
            R.color.teal_700
        )
        BitmapHelper.vectorToBitmap(
            context,
            R.drawable.ic_baseline_place_24,
            color
        )
    }

    /**
     * Method called before the cluster item (the marker) is rendered.
     * This is where marker options should be set.
     */
    override fun onBeforeClusterItemRendered(
        item: Asset,
        markerOptions: MarkerOptions
    ) {
        markerOptions.title(item.name)
            .position(item.latLng)
            .icon(assetIcon)
    }

    /**
     * Method called right after the cluster item (the marker) is rendered.
     * This is where properties for the Marker object should be set.
     */
    override fun onClusterItemRendered(clusterItem: Asset, marker: Marker) {
        marker.tag = clusterItem
    }
}