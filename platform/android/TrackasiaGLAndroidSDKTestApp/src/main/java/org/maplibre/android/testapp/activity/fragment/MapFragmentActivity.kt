package org.trackasia.android.testapp.activity.fragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.trackasia.android.camera.CameraPosition
import org.trackasia.android.camera.CameraUpdateFactory
import org.trackasia.android.geometry.LatLng
import org.trackasia.android.maps.* // ktlint-disable no-wildcard-imports
import org.trackasia.android.maps.MapFragment.OnMapViewReadyCallback
import org.trackasia.android.maps.MapView.OnDidFinishRenderingFrameListener
import org.trackasia.android.testapp.R

/**
 * Test activity showcasing using the MapFragment API using SDK Fragments.
 *
 *
 * Uses MapboxMapOptions to initialise the Fragment.
 *
 */
class MapFragmentActivity :
    AppCompatActivity(),
    OnMapViewReadyCallback,
    OnMapReadyCallback,
    OnDidFinishRenderingFrameListener {
    private lateinit var trackasiaMap: trackasiaMap
    private lateinit var mapView: MapView
    private var initialCameraAnimation = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_fragment)
        val mapFragment: MapFragment
        if (savedInstanceState == null) {
            mapFragment = MapFragment.newInstance(createFragmentOptions())
            fragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, mapFragment, TAG)
                .commit()
        } else {
            mapFragment = fragmentManager.findFragmentByTag(TAG) as MapFragment
        }
        mapFragment.getMapAsync(this)
    }

    private fun createFragmentOptions(): trackasiaMapOptions {
        val options = trackasiaMapOptions.createFromAttributes(this, null)
        options.scrollGesturesEnabled(false)
        options.zoomGesturesEnabled(false)
        options.tiltGesturesEnabled(false)
        options.rotateGesturesEnabled(false)
        options.debugActive(false)
        val dc = LatLng(38.90252, -77.02291)
        options.minZoomPreference(9.0)
        options.maxZoomPreference(11.0)
        options.camera(
            CameraPosition.Builder()
                .target(dc)
                .zoom(11.0)
                .build()
        )
        return options
    }

    override fun onMapViewReady(map: MapView) {
        mapView = map
        mapView.addOnDidFinishRenderingFrameListener(this)
    }

    override fun onMapReady(map: trackasiaMap) {
        trackasiaMap = map
        trackasiaMap.setStyle(Style.getPredefinedStyle("Outdoor"))
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mapView != null) {
            mapView.removeOnDidFinishRenderingFrameListener(this)
        }
    }

    override fun onDidFinishRenderingFrame(fully: Boolean) {
        if (initialCameraAnimation && fully && trackasiaMap != null) {
            trackasiaMap.animateCamera(
                CameraUpdateFactory.newCameraPosition(CameraPosition.Builder().tilt(45.0).build()),
                5000
            )
            initialCameraAnimation = false
        }
    }

    companion object {
        private const val TAG = "com.mapbox.map"
    }
}
