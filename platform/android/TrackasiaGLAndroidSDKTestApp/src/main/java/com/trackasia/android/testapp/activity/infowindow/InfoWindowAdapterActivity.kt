package com.trackasia.android.testapp.activity.infowindow

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.trackasia.android.annotations.Marker
import com.trackasia.android.geometry.LatLng
import com.trackasia.android.maps.MapView
import com.trackasia.android.maps.TrackasiaMap
import com.trackasia.android.maps.TrackasiaMap.InfoWindowAdapter
import com.trackasia.android.maps.OnMapReadyCallback
import com.trackasia.android.maps.Style
import com.trackasia.android.testapp.R
import com.trackasia.android.testapp.model.annotations.CityStateMarker
import com.trackasia.android.testapp.model.annotations.CityStateMarkerOptions
import com.trackasia.android.testapp.utils.IconUtils

/**
 * Test activity showcasing using an InfoWindowAdapter to provide a custom InfoWindow content.
 */
class InfoWindowAdapterActivity : AppCompatActivity() {
    private lateinit var mapView: MapView
    private lateinit var mapboxMap: TrackasiaMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_infowindow_adapter)
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(
            OnMapReadyCallback { map: TrackasiaMap ->
                mapboxMap = map
                map.setStyle(Style.getPredefinedStyle("Streets")) { style: Style? ->
                    addMarkers()
                    addCustomInfoWindowAdapter()
                }
            }
        )
    }

    private fun addMarkers() {
        mapboxMap!!.addMarker(generateCityStateMarker("Andorra", 42.505777, 1.52529, "#F44336"))
        mapboxMap!!.addMarker(generateCityStateMarker("Luxembourg", 49.815273, 6.129583, "#3F51B5"))
        mapboxMap!!.addMarker(generateCityStateMarker("Monaco", 43.738418, 7.424616, "#673AB7"))
        mapboxMap!!.addMarker(
            generateCityStateMarker(
                "Vatican City",
                41.902916,
                12.453389,
                "#009688"
            )
        )
        mapboxMap!!.addMarker(
            generateCityStateMarker(
                "San Marino",
                43.942360,
                12.457777,
                "#795548"
            )
        )
        mapboxMap!!.addMarker(
            generateCityStateMarker(
                "Liechtenstein",
                47.166000,
                9.555373,
                "#FF5722"
            )
        )
    }

    private fun generateCityStateMarker(
        title: String,
        lat: Double,
        lng: Double,
        color: String
    ): CityStateMarkerOptions {
        val marker = CityStateMarkerOptions()
        marker.title(title)
        marker.position(LatLng(lat, lng))
        marker.infoWindowBackground(color)
        val icon =
            IconUtils.drawableToIcon(this, R.drawable.ic_location_city, Color.parseColor(color))
        marker.icon(icon)
        return marker
    }

    private fun addCustomInfoWindowAdapter() {
        mapboxMap!!.infoWindowAdapter = object : InfoWindowAdapter {
            private val tenDp = resources.getDimension(R.dimen.attr_margin).toInt()
            override fun getInfoWindow(marker: Marker): View? {
                val textView = TextView(this@InfoWindowAdapterActivity)
                textView.text = marker.title
                textView.setTextColor(Color.WHITE)
                if (marker is CityStateMarker) {
                    textView.setBackgroundColor(Color.parseColor(marker.infoWindowBackgroundColor))
                }
                textView.setPadding(tenDp, tenDp, tenDp, tenDp)
                return textView
            }
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
