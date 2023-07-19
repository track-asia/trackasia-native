package com.trackasia.android.testapp.activity.camera

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.trackasia.android.maps.MapView
import com.trackasia.android.maps.MapView.OnDidFinishLoadingStyleListener
import com.trackasia.android.maps.TrackasiaMap
import com.trackasia.android.maps.TrackasiaMap.OnMapClickListener
import com.trackasia.android.maps.OnMapReadyCallback
import com.trackasia.android.maps.Style
import com.trackasia.android.testapp.R
import timber.log.Timber

/** Test activity showcasing using maximum and minimum zoom levels to restrict camera movement. */
class MaxMinZoomActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mapView: MapView
    private lateinit var trackasiaMap: TrackasiaMap
    private val clickListener = OnMapClickListener {
        if (trackasiaMap != null) {
            trackasiaMap.setStyle(Style.Builder().fromUri(Style.getPredefinedStyle("Outdoor")))
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maxmin_zoom)
        mapView = findViewById<MapView>(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        mapView.addOnDidFinishLoadingStyleListener(
            OnDidFinishLoadingStyleListener { Timber.d("Style Loaded") }
        )
    }

    override fun onMapReady(map: TrackasiaMap) {
        trackasiaMap = map
        trackasiaMap.setStyle(Style.getPredefinedStyle("Streets"))
        trackasiaMap.setMinZoomPreference(3.0)
        trackasiaMap.setMaxZoomPreference(5.0)
        trackasiaMap.addOnMapClickListener(clickListener)
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
        if (trackasiaMap != null) {
            trackasiaMap.removeOnMapClickListener(clickListener)
        }
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
