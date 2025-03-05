package com.trackasia.android.testapp.activity.textureview

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.trackasia.android.camera.CameraPosition
import com.trackasia.android.geometry.LatLng
import com.trackasia.android.maps.*
import com.trackasia.android.testapp.R
import com.trackasia.android.testapp.utils.ResourceUtils
import timber.log.Timber
import java.io.IOException

/**
 * Example showcasing how to create a TextureView with a transparent background.
 */
class TextureViewTransparentBackgroundActivity : AppCompatActivity() {
    private lateinit var mapView: MapView
    private val trackasiaMap: TrackAsiaMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_textureview_transparent)
        setupBackground()
        setupMapView(savedInstanceState)
    }

    private fun setupBackground() {
        val imageView = findViewById<ImageView>(R.id.imageView)
        imageView.setImageResource(R.drawable.water)
        imageView.scaleType = ImageView.ScaleType.FIT_XY
    }

    private fun setupMapView(savedInstanceState: Bundle?) {
        val trackasiaMapOptions = TrackAsiaMapOptions.createFromAttributes(this, null)
        trackasiaMapOptions.translucentTextureSurface(true)
        trackasiaMapOptions.textureMode(true)
        trackasiaMapOptions.camera(
            CameraPosition.Builder()
                .zoom(2.0)
                .target(LatLng(48.507879, 8.363795))
                .build()
        )
        mapView = MapView(this, trackasiaMapOptions)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { trackasiaMap: TrackAsiaMap -> initMap(trackasiaMap) }
        (findViewById<View>(R.id.coordinator_layout) as ViewGroup).addView(mapView)
    }

    private fun initMap(trackasiaMap: TrackAsiaMap) {
        try {
            trackasiaMap.setStyle(
                Style.Builder().fromJson(ResourceUtils.readRawResource(this, R.raw.no_bg_style))
            )
        } catch (exception: IOException) {
            Timber.e(exception)
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
