package org.trackasia.android.testapp.activity.espresso

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.trackasia.android.maps.MapView
import org.trackasia.android.maps.OnMapReadyCallback
import org.trackasia.android.maps.Style
import org.trackasia.android.maps.TrackAsiaMap
import org.trackasia.android.testapp.R

/**
 * Test activity used for instrumentation tests that require a specific device size.
 */
class PixelTestActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var mapView: MapView
    lateinit var trackasiaMap: TrackAsiaMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pixel_test)
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(map: TrackAsiaMap) {
        trackasiaMap = map
        val styleURL = Style.getPredefinedStyles()[0].url
        trackasiaMap.setStyle(styleURL)
    }

    public override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    public override fun onPause() {
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