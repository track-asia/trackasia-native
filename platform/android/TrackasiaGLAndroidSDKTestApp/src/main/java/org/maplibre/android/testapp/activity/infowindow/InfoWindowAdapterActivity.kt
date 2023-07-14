package org.trackasia.android.testapp.activity.infowindow

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.trackasia.android.annotations.Marker
import org.trackasia.android.geometry.LatLng
import org.trackasia.android.maps.MapView
import org.trackasia.android.maps.trackasiaMap
import org.trackasia.android.maps.trackasiaMap.InfoWindowAdapter
import org.trackasia.android.maps.OnMapReadyCallback
import org.trackasia.android.maps.Style
import org.trackasia.android.testapp.R
import org.trackasia.android.testapp.model.annotations.CityStateMarker
import org.trackasia.android.testapp.model.annotations.CityStateMarkerOptions
import org.trackasia.android.testapp.utils.IconUtils

/**
 * Test activity showcasing using an InfoWindowAdapter to provide a custom InfoWindow content.
 */
class InfoWindowAdapterActivity : AppCompatActivity() {
    private lateinit var mapView: MapView
    private lateinit var trackasiaMap: trackasiaMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_infowindow_adapter)
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(
            OnMapReadyCallback { map: trackasiaMap ->
                trackasiaMap = map
                map.setStyle(Style.getPredefinedStyle("Streets")) { style: Style? ->
                    addMarkers()
                    addCustomInfoWindowAdapter()
                }
            }
        )
    }

    private fun addMarkers() {
        trackasiaMap.addMarker(generateCityStateMarker("Andorra", 42.505777, 1.52529, "#F44336"))
        trackasiaMap.addMarker(generateCityStateMarker("Luxembourg", 49.815273, 6.129583, "#3F51B5"))
        trackasiaMap.addMarker(generateCityStateMarker("Monaco", 43.738418, 7.424616, "#673AB7"))
        trackasiaMap.addMarker(
            generateCityStateMarker(
                "Vatican City",
                41.902916,
                12.453389,
                "#009688"
            )
        )
        trackasiaMap.addMarker(
            generateCityStateMarker(
                "San Marino",
                43.942360,
                12.457777,
                "#795548"
            )
        )
        trackasiaMap.addMarker(
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
        trackasiaMap.infoWindowAdapter = object : InfoWindowAdapter {
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
}
