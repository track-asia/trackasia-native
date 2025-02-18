package com.trackasia.android.testapp.activity.maplayout

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.trackasia.android.annotations.MarkerOptions
import com.trackasia.android.camera.CameraPosition
import com.trackasia.android.camera.CameraUpdateFactory
import com.trackasia.android.geometry.LatLng
import com.trackasia.android.maps.*
import com.trackasia.android.testapp.R
import com.trackasia.android.testapp.styles.TestStyles

/**
 * Test activity showcasing using the map padding API.
 */
class MapPaddingActivity : AppCompatActivity() {
    private lateinit var mapView: MapView
    private lateinit var trackasiaMap: TrackAsiaMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_padding)
        mapView = findViewById(R.id.mapView)
        mapView.setTag(true)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            trackasiaMap = it
            it.setStyle(TestStyles.getPredefinedStyleWithFallback("Streets"))
            val paddingLeft = resources.getDimension(R.dimen.map_padding_left).toInt()
            val paddingBottom = resources.getDimension(R.dimen.map_padding_bottom).toInt()
            val paddingRight = resources.getDimension(R.dimen.map_padding_right).toInt()
            val paddingTop = resources.getDimension(R.dimen.map_padding_top).toInt()
            it.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
            val settings = it.uiSettings
            settings.setLogoMargins(paddingLeft, 0, 0, paddingBottom)
            settings.setCompassMargins(0, paddingTop, paddingRight, 0)
            settings.isAttributionEnabled = false
            moveToBangalore()
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_padding, menu)
        return true
    }

    private fun moveToBangalore() {
        val bangalore = LatLng(12.9810816, 77.6368034)
        val cameraPosition =
            CameraPosition
                .Builder()
                .zoom(16.0)
                .target(bangalore)
                .bearing(40.0)
                .tilt(45.0)
                .build()
        trackasiaMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        trackasiaMap.addMarker(MarkerOptions().title("Center map").position(bangalore))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.action_bangalore -> {
                if (this::trackasiaMap.isInitialized) {
                    moveToBangalore()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}
