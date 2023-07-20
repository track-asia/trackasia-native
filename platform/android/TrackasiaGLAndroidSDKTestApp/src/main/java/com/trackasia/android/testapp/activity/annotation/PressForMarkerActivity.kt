package com.trackasia.android.testapp.activity.annotation

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.trackasia.android.annotations.MarkerOptions
import com.trackasia.android.geometry.LatLng
import com.trackasia.android.maps.MapView
import com.trackasia.android.maps.Style
import com.trackasia.android.maps.TrackasiaMap
import com.trackasia.android.testapp.R
import java.text.DecimalFormat
import java.util.ArrayList

/**
 * Test activity showcasing to add a Marker on click.
 *
 *
 * Shows how to use a OnMapClickListener and a OnMapLongClickListener
 *
 */
class PressForMarkerActivity : AppCompatActivity() {
    private lateinit var mapView: MapView
    private lateinit var trackasiaMap: TrackasiaMap
    private var markerList: ArrayList<MarkerOptions>? = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_press_for_marker)
        mapView = findViewById<View>(R.id.mapView) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { map: TrackasiaMap? ->
            if (map != null) {
                trackasiaMap = map
            }
            resetMap()
            trackasiaMap.addOnMapLongClickListener { point: LatLng ->
                addMarker(point)
                false
            }
            trackasiaMap.addOnMapClickListener { point: LatLng ->
                addMarker(point)
                false
            }
            trackasiaMap.setStyle(Style.getPredefinedStyle("Streets"))
            if (savedInstanceState != null) {
                markerList = savedInstanceState.getParcelableArrayList(STATE_MARKER_LIST)
                if (markerList != null) {
                    trackasiaMap.addMarkers(markerList!!)
                }
            }
        }
    }

    private fun addMarker(point: LatLng) {
        val pixel = trackasiaMap.projection.toScreenLocation(point)
        val title = (
            LAT_LON_FORMATTER.format(point.latitude) + ", " +
                LAT_LON_FORMATTER.format(point.longitude)
            )
        val snippet = "X = " + pixel.x.toInt() + ", Y = " + pixel.y.toInt()
        val marker = MarkerOptions()
            .position(point)
            .title(title)
            .snippet(snippet)
        markerList!!.add(marker)
        trackasiaMap.addMarker(marker)
    }

    private fun resetMap() {
        if (trackasiaMap == null) {
            return
        }
        markerList!!.clear()
        trackasiaMap.removeAnnotations()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_press_for_marker, menu)
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
        outState.putParcelableArrayList(STATE_MARKER_LIST, markerList)
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

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuItemReset -> {
                resetMap()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private val LAT_LON_FORMATTER = DecimalFormat("#.#####")
        private const val STATE_MARKER_LIST = "markerList"
    }
}
