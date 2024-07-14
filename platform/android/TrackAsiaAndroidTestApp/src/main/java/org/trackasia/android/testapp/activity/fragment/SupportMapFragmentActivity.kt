package org.trackasia.android.testapp.activity.fragment

import android.os.Bundle // ktlint-disable import-ordering
import androidx.appcompat.app.AppCompatActivity
import org.trackasia.android.camera.CameraPosition
import org.trackasia.android.camera.CameraUpdateFactory
import org.trackasia.android.geometry.LatLng
import org.trackasia.android.maps.* // ktlint-disable no-wildcard-imports
import org.trackasia.android.maps.MapFragment.OnMapViewReadyCallback
import org.trackasia.android.maps.MapView.OnDidFinishRenderingFrameListener
import org.trackasia.android.testapp.R

/**
 * Test activity showcasing using the MapFragment API using Support Library Fragments.
 *
 *
 * Uses MapboxMapOptions to initialise the Fragment.
 *
 */
class SupportMapFragmentActivity :
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
        val mapFragment: SupportMapFragment?
        if (savedInstanceState == null) {
            mapFragment = SupportMapFragment.newInstance(createFragmentOptions())
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, mapFragment, TAG)
                .commit()
        } else {
            mapFragment = supportFragmentManager.findFragmentByTag(TAG) as SupportMapFragment?
        }
        mapFragment!!.getMapAsync(this)
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

    override fun onMapReady(map: TrackAsiaMap) {
        trackasiaMap = map
        trackasiaMap.setStyle(Style.getPredefinedStyle("Satellite Hybrid"))
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.removeOnDidFinishRenderingFrameListener(this)
    }

    override fun onDidFinishRenderingFrame(fully: Boolean, frameEncodingTime: Double, frameRenderingTime: Double) {
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
