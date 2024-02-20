package com.trackasia.android.testapp.activity.style

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.trackasia.android.maps.*
import com.trackasia.android.style.layers.*
import com.trackasia.android.style.sources.VectorSource
import com.trackasia.android.testapp.R

/**
 * Test activity for unit test execution
 */
class RuntimeStyleTimingTestActivity : AppCompatActivity() {
    var mapView: MapView? = null
    var mapboxMap: TrackasiaMap? = null
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_runtime_style)

        // Initialize map as normal
        mapView = findViewById<View>(R.id.mapView) as MapView
        mapView!!.onCreate(savedInstanceState)
        mapView!!.getMapAsync { mapboxMap: TrackasiaMap ->
            this@RuntimeStyleTimingTestActivity.mapboxMap = mapboxMap
            val parksLayer = CircleLayer("parks", "parks_source")
            parksLayer.sourceLayer = "parks"
            parksLayer.setProperties(
                PropertyFactory.visibility(Property.VISIBLE),
                PropertyFactory.circleRadius(8f),
                PropertyFactory.circleColor(Color.argb(1, 55, 148, 179))
            )
            val parks = VectorSource(
                "parks_source",
                "maptiler://sources/7ac429c7-c96e-46dd-8c3e-13d48988986a"
            )
            mapboxMap.setStyle(
                Style.Builder()
                    .fromUri("https://tiles.track-asia.com/tiles/v1/style-streets.json?key=public")
                    .withSource(parks)
                    .withLayer(parksLayer)
            )
        }
    }

    override fun onStart() {
        super.onStart()
        mapView!!.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView!!.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView!!.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView!!.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView!!.onLowMemory()
    }
}
