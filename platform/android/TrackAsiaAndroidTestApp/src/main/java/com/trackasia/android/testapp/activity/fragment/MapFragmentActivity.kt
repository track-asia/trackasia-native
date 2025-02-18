@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.trackasia.android.testapp.activity.fragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.trackasia.android.camera.CameraPosition
import com.trackasia.android.camera.CameraUpdateFactory
import com.trackasia.android.geometry.LatLng
import com.trackasia.android.maps.*
import com.trackasia.android.maps.MapFragment.OnMapViewReadyCallback
import com.trackasia.android.maps.MapView.OnDidFinishRenderingFrameListener
import com.trackasia.android.testapp.R
import com.trackasia.android.testapp.styles.TestStyles

/**
 * Test activity showcasing using the MapFragment API using SDK Fragments.
 *
 *
 * Uses TrackAsiaMapOptions to initialise the Fragment.
 *
 */
class MapFragmentActivity :
    AppCompatActivity(),
    OnMapViewReadyCallback,
    OnMapReadyCallback,
    OnDidFinishRenderingFrameListener {
    private lateinit var trackasiaMap: TrackAsiaMap
    private lateinit var mapView: MapView
    private var initialCameraAnimation = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_fragment)
        val mapFragment: MapFragment
        if (savedInstanceState == null) {
            mapFragment = MapFragment.newInstance(createFragmentOptions())
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, mapFragment, TAG)
                .commit()
        } else {
            mapFragment = supportFragmentManager.findFragmentByTag(TAG) as MapFragment
        }
        mapFragment.getMapAsync(this)
    }

    private fun createFragmentOptions(): TrackAsiaMapOptions {
        val options = TrackAsiaMapOptions.createFromAttributes(this, null)
        options.scrollGesturesEnabled(false)
        options.zoomGesturesEnabled(false)
        options.tiltGesturesEnabled(false)
        options.rotateGesturesEnabled(false)
        options.debugActive(false)
        val dc = LatLng(38.90252, -77.02291)
        options.minZoomPreference(9.0)
        options.maxZoomPreference(11.0)
        options.camera(
            CameraPosition
                .Builder()
                .target(dc)
                .zoom(11.0)
                .build(),
        )
        return options
    }

    override fun onMapViewReady(map: MapView) {
        mapView = map
        mapView.addOnDidFinishRenderingFrameListener(this)
    }

    override fun onMapReady(map: TrackAsiaMap) {
        trackasiaMap = map
        trackasiaMap.setStyle(TestStyles.getPredefinedStyleWithFallback("Outdoor"))
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::mapView.isInitialized) {
            mapView.removeOnDidFinishRenderingFrameListener(this)
        }
    }

    override fun onDidFinishRenderingFrame(
        fully: Boolean,
        frameEncodingTime: Double,
        frameRenderingTime: Double,
    ) {
        if (initialCameraAnimation && fully && this::trackasiaMap.isInitialized) {
            trackasiaMap.animateCamera(
                CameraUpdateFactory.newCameraPosition(CameraPosition.Builder().tilt(45.0).build()),
                5000,
            )
            initialCameraAnimation = false
        }
    }

    companion object {
        private const val TAG = "com.mapbox.map"
    }
}
