package com.trackasia.android.testapp.activity.espresso

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.trackasia.android.maps.MapView
import com.trackasia.android.maps.TrackAsiaMap
import com.trackasia.android.testapp.R

/**
 * Base activity for instrumentation testing.
 */
class EspressoTestActivity : AppCompatActivity() {
    lateinit var mapView: MapView
    protected lateinit var trackasiaMap: TrackAsiaMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_espresso_test)
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
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
