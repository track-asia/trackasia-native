package com.trackasia.android.testapp.activity.fragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.trackasia.android.camera.CameraPosition
import com.trackasia.android.camera.CameraUpdateFactory
import com.trackasia.android.geometry.LatLng
import com.trackasia.android.maps.* // ktlint-disable no-wildcard-imports
import com.trackasia.android.maps.MapFragment.OnMapViewReadyCallback
import com.trackasia.android.maps.MapView.OnDidFinishRenderingFrameListener
import com.trackasia.android.testapp.R

/**
 * Test activity showcasing using the MapFragment API using SDK Fragments.
 *
 *
 * Uses TrackasiaMapOptions to initialise the Fragment.
 *
 */
class MapFragmentActivity :
    AppCompatActivity(),
    OnMapViewReadyCallback,
    OnMapReadyCallback,
    OnDidFinishRenderingFrameListener {
    private var mapboxMap: TrackasiaMap? = null
    private var mapView: MapView? = null
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

    private fun createFragmentOptions(): TrackasiaMapOptions {
        val options = TrackasiaMapOptions.createFromAttributes(this, null)
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
        mapView!!.addOnDidFinishRenderingFrameListener(this)
    }

    override fun onMapReady(map: TrackasiaMap) {
        mapboxMap = map
        mapboxMap!!.setStyle("https://tiles.track-asia.com/tiles/v1/style-streets.json?key=public")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mapView != null) {
            mapView!!.removeOnDidFinishRenderingFrameListener(this)
        }
    }

    override fun onDidFinishRenderingFrame(fully: Boolean) {
        if (initialCameraAnimation && fully && mapboxMap != null) {
            mapboxMap!!.animateCamera(
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
