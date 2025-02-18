package com.trackasia.android.testapp.activity.style

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.trackasia.android.camera.CameraUpdateFactory
import com.trackasia.android.geometry.LatLng
import com.trackasia.android.maps.Style
import com.trackasia.android.style.layers.PropertyFactory.iconImage
import com.trackasia.android.style.layers.SymbolLayer
import com.trackasia.android.style.sources.GeoJsonSource
import com.trackasia.android.testapp.R
import com.trackasia.android.testapp.databinding.ActivityMapSimpleBinding
import java.net.URI

/**
 * Activity showcasing how to load symbols on a map without a Style URI or Style JSON.
 */
class NoStyleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapSimpleBinding

    private val imageIcon: Drawable by lazy {
        ResourcesCompat.getDrawable(resources, R.drawable.ic_add_white, theme)!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapSimpleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // # --8<-- [start:setup]
        binding.mapView.getMapAsync { map ->
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(cameraTarget, cameraZoom))
            map.setStyle(
                Style
                    .Builder()
                    .withImage(imageId, imageIcon)
                    .withSource(GeoJsonSource(sourceId, URI("asset://points-sf.geojson")))
                    .withLayer(SymbolLayer(layerId, sourceId).withProperties(iconImage(imageId))),
            )
        }
        // # --8<-- [end:setup]
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onSaveInstanceState(
        outState: Bundle,
        outPersistentState: PersistableBundle,
    ) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState?.let {
            binding.mapView.onSaveInstanceState(it)
        }
    }

    companion object {
        const val layerId = "custom-layer-id"
        const val sourceId = "custom-source-id"
        const val imageId = "image-id"
        const val cameraZoom = 10.0
        val cameraTarget = LatLng(37.758912, -122.442578)
    }
}
